package app.cryptarch.controller;

import app.cryptarch.service.impl.CryptoApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/external/crypto")
@RequiredArgsConstructor
public class CryptoApiController {

    private final CryptoApiServiceImpl cryptoApiServiceImpl;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<Map<String,Object>> getPriceFromApi(@PathVariable String symbol) {
        Map<String, Object> priceData = cryptoApiServiceImpl.fetchPriceFromApi(symbol.toLowerCase());

        if(priceData.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Double price = null;
        if (priceData.containsKey(symbol.toLowerCase())) {
            Map<String, Object> symbolData = (Map<String, Object>) priceData.get(symbol.toLowerCase());
            price = (Double) symbolData.get("usd");
        }

        return ResponseEntity.ok(Map.of("price", price));
    }
}
