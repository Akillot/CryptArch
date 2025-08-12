package app.cryptarch.controller;

import app.cryptarch.dto.CryptoDto;
import app.cryptarch.dto.PriceDto;
import app.cryptarch.service.CoinGeckoRateLimiter;
import app.cryptarch.service.OverviewService;
import app.cryptarch.service.impl.CryptoApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoApiController {

    private final CryptoApiServiceImpl cryptoApiServiceImpl;
    private final CoinGeckoRateLimiter rateLimiter;

    // Example: http://localhost:8081/api/crypto/overview/bitcoin?fiat=usd&days=7
    @GetMapping("/{id}/overview")
    public ResponseEntity<?> overview(
            @PathVariable String id,
            @RequestParam(defaultValue = "usd") String fiat,
            @RequestParam(defaultValue = "7") int days,
            OverviewService overviewService
    ) {
        try {
            var dto = overviewService.getOverview(id, fiat, days);
            return ResponseEntity.ok(dto);
        } catch (OverviewService.RateLimitException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", e.getMessage()));
        } catch (OverviewService.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to build overview"));
        }
    }

    // Example: http://localhost:8081/api/crypto/price/bitcoin/usd
    @GetMapping("/price/{symbol}/{fiatcode}")
    public ResponseEntity<?> getPriceFromApi(@PathVariable String symbol, @PathVariable String fiatcode) {
        String crypto = symbol.toLowerCase();
        String fiat = fiatcode.toLowerCase();

        Map<String, Object> priceData = cryptoApiServiceImpl.fetchPriceFromApi(crypto, fiat);

        if (isPriceDataInvalid(priceData, crypto, fiat)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No price data available for " + crypto + " in " + fiat));
        }

        double price = extractPrice(priceData, crypto, fiat);
        return ResponseEntity.ok(new PriceDto(crypto, fiat, price));
    }

    private boolean isPriceDataInvalid(Map<String, Object> priceData, String crypto, String fiat) {
        if (priceData == null || priceData.isEmpty()) return true;

        Object symbolDataObj = priceData.get(crypto);
        if (!(symbolDataObj instanceof Map<?, ?> symbolData)) return true;

        return !symbolData.containsKey(fiat);
    }

    private double extractPrice(Map<String, Object> priceData, String crypto, String fiat) {
        Object symbolDataObj = priceData.get(crypto);
        if (symbolDataObj instanceof Map<?, ?> symbolData) {
            Object priceObj = symbolData.get(fiat);
            if (priceObj instanceof Number number) {
                return number.doubleValue();
            }
        }
        return 0.0;
    }

    // Example: http://localhost:8081/api/crypto/prices?symbols=bitcoin,ethereum,solana&fiat=usd
    @GetMapping("/prices")
    public ResponseEntity<?> getPricesForMultipleCryptos(@RequestParam List<String> symbols, @RequestParam String fiat) {
        String fiatLower = fiat.toLowerCase();
        List<String> cryptoSymbols = symbols.stream().map(String::toLowerCase).toList();

        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Rate limit reached, please wait"));
        }

        Map<String, Object> prices = cryptoApiServiceImpl.fetchPriceForMultipleCryptos(cryptoSymbols, fiatLower);

        if (prices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No price data available for requested cryptocurrencies"));
        }

        List<PriceDto> dtoList = prices.entrySet().stream()
                .map(entry -> {
                    String cryptoId = entry.getKey();
                    Object priceObj = ((Map<?, ?>) entry.getValue()).get(fiatLower);
                    double price = (priceObj instanceof Number number) ? number.doubleValue() : 0.0;
                    return new PriceDto(cryptoId, fiatLower, price);
                })
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // Example: http://localhost:8081/api/crypto/list
    @GetMapping("/list")
    public ResponseEntity<?> getListPrices() {
        var cryptos = cryptoApiServiceImpl.fetchAllCryptos().stream()
                .map(c -> new CryptoDto(
                        (String) c.get("id"),
                        (String) c.get("symbol"),
                        (String) c.get("name")
                ))
                .toList();

        if (cryptos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No crypto list data available"));
        }

        return ResponseEntity.ok(cryptos);
    }
}
