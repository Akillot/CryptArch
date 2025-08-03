package app.cryptarch.controller;

import app.cryptarch.service.impl.CryptoApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external/crypto")
@RequiredArgsConstructor
public class CryptoApiController {

    private final CryptoApiServiceImpl cryptoApiServiceImpl;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<Map<String, Object>> getPriceFromApi(@PathVariable String symbol) {
        Map<String, Object> priceData = cryptoApiServiceImpl.fetchPriceFromApi(symbol.toLowerCase());

        if (priceData == null || priceData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No price data available for " + symbol));
        }

        if (priceData.containsKey(symbol.toLowerCase())) {
            Map<String, Object> symbolData = (Map<String, Object>) priceData.get(symbol.toLowerCase());
            if (symbolData != null && symbolData.containsKey("usd")) {
                Number priceNumber = (Number) symbolData.get("usd");
                return ResponseEntity.ok(Map.of("price", priceNumber.doubleValue()));
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected API response format for " + symbol));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListPrices() {
        List<Map<String,Object>> cryptos = cryptoApiServiceImpl.fetchAllCryptos();

        if (cryptos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No price data available for crypto"));
        }

        return ResponseEntity.ok(cryptos);
    }
}
