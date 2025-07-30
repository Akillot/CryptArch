package app.cryptarch.service.impl;

import app.cryptarch.service.CryptoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptoApiServiceImpl implements CryptoApiService {

    private final RestTemplate restTemplate;

    @Override
    public Map<String, Object> fetchPriceFromApi(String symbol) {
        String url = "https://api.coingecko.com/api/v3/simple/price?ids="
        + symbol + "&vs_currencies=usd";
        return restTemplate.getForObject(url, Map.class);
    }
}
