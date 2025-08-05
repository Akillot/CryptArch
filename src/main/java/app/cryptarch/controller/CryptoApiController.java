package app.cryptarch.controller;

import app.cryptarch.service.CoinGeckoRateLimiter;
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

    @GetMapping("/price/{symbol}/{fiatcode}")
    public ResponseEntity<Map<String, Object>> getPriceFromApi(@PathVariable String symbol, @PathVariable String fiatcode) {
        String crypto = symbol.toLowerCase();
        String fiat = fiatcode.toLowerCase();

        Map<String, Object> priceData = cryptoApiServiceImpl.fetchPriceFromApi(crypto, fiat);

        if (isPriceDataInvalid(priceData, crypto, fiat)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No price data available for " + crypto + " in " + fiat));
        }

        double price = extractPrice(priceData, crypto, fiat);
        return ResponseEntity.ok(Map.of("price", price));
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

    //Example: http://localhost:8081/api/crypto/prices?symbols=bitcoin,ethereum,solana&fiat=usd
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

        return ResponseEntity.ok(prices);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListPrices() {
        List<Map<String, Object>> cryptos = cryptoApiServiceImpl.fetchAllCryptos();

        if (cryptos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No price data available for crypto"));
        }

        return ResponseEntity.ok(cryptos);
    }
}
