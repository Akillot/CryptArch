package app.cryptarch.service.impl;

import app.cryptarch.service.CoinGeckoRateLimiter;
import app.cryptarch.service.CryptoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptoApiServiceImpl implements CryptoApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CoinGeckoRateLimiter rateLimiter;

    private Map<String, Object> lastPriceCache = new HashMap<>();
    private long lastCacheTime = 0;

    @Override
    public synchronized Map<String, Object> fetchPriceFromApi(String symbol, String fiatCurrencyCode) {
        long now = Instant.now().toEpochMilli();
        String cacheKey = symbol + "_" + fiatCurrencyCode;

        if(now - lastCacheTime < 12_000 && lastPriceCache.containsKey(symbol)) {
            return (Map<String, Object>) lastPriceCache.get(cacheKey);
        }

        if(!rateLimiter.tryConsume()){
            return Map.of("Error", "Rate limit reached, wait");
        }

        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + symbol + "&vs_currencies=" + fiatCurrencyCode;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if(response != null){
            lastPriceCache.put(symbol, response);
            lastCacheTime = now;
        }

        return response != null ? response : Map.of();
    }

    public List<Map<String, Object>> fetchAllCryptos(){
        String url = "https://api.coingecko.com/api/v3/coins/list";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        return response != null ? response : List.of();
    }

    public Map<String, Object> fetchPriceForMultipleCryptos(List<String> symbols, String fiat) {
        if (!rateLimiter.tryConsume()) {
            return Map.of("error", "Rate limit reached, wait");
        }

        String ids = String.join(",", symbols);
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=" + fiat;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return response != null ? response : Map.of();
    }

    // In progress
    public List<Map<String, Object>> fetchAllCryptoInfo(){
        String url = "https://api.coingecko.com/api/v3/coins/markets";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        return response != null ? response : List.of();
    }
}
