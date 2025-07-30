package app.cryptarch.controller;

import app.cryptarch.service.CryptoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/external/crypto")
@RequiredArgsConstructor
public class CryptoApiController {

    private final CryptoApiService cryptoApiService;

    @GetMapping("/price/{symbol}")
    public Map<String, Object> getPriceFromApi(@PathVariable String symbol){
        return cryptoApiService.fetchPriceFromApi(symbol);
    }
}
