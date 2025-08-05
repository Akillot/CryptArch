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
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoApiController {

    private final CryptoApiServiceImpl cryptoApiServiceImpl;

    @GetMapping("/price/{symbol}/{fiatcode}")
    public ResponseEntity<Map<String, Object>> getPriceFromApi(@PathVariable String symbol, @PathVariable String fiatcode) {
        String crypto = symbol.toLowerCase();
        String fiat = fiatcode.toLowerCase();

        Map<String, Object> priceData = cryptoApiServiceImpl.fetchPriceFromApi(crypto, fiatcode);

        if (isPriceDataInvalid(priceData,crypto, fiatcode)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No price data available for " + crypto + " in " + fiat));
        }
        double price = extractPrice(priceData,crypto, fiatcode);
        return ResponseEntity.ok(Map.of("price", price));
    }

    private boolean isPriceDataInvalid(Map<String, Object> priceData, String crypto, String fiat) {
        if (priceData == null || priceData.isEmpty()) return true;
        Map<String, Object> symbolData = (Map<String, Object>) priceData.get(crypto);
        return symbolData == null || !symbolData.containsKey(fiat);
    }

    private double extractPrice(Map<String, Object> priceData, String crypto, String fiat) {
        Map<String, Object> symbolData = (Map<String, Object>) priceData.get(crypto);
        return ((Number) symbolData.get(fiat)).doubleValue();
    }

    //Get all cryptocurrencies
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
