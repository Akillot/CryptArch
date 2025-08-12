package app.cryptarch.service.impl;

import app.cryptarch.service.CoinGeckoRateLimiter;
import app.cryptarch.service.CryptoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CryptoApiServiceImpl implements CryptoApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CoinGeckoRateLimiter rateLimiter;

    private static class CacheEntry {
        Map<String, Object> data;
        long ts;
    }
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public synchronized Map<String, Object> fetchPriceFromApi(String symbol, String fiat) {
        String key = symbol + "_" + fiat;
        long now = Instant.now().toEpochMilli();
        CacheEntry e = cache.get(key);

        if (e != null && now - e.ts < 12_000) {
            return e.data;
        }

        if (!rateLimiter.tryConsume()) return Map.of();


        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + symbol + "&vs_currencies=" + fiat;
        Map<String, Object> resp = restTemplate.getForObject(url, Map.class);

        if (resp != null) {
            CacheEntry newEntry = new CacheEntry();
            newEntry.data = resp;
            newEntry.ts = now;
            cache.put(key, newEntry);
        }

        return resp != null ? resp : Map.of();
    }

    public List<Map<String, Object>> fetchAllCryptos() {
        String url = "https://api.coingecko.com/api/v3/coins/list";
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        return response != null ? response : List.of();
    }

    public Map<String, Object> fetchPriceForMultipleCryptos(List<String> symbols, String fiat) {
        if (!rateLimiter.tryConsume()) {
            return Map.of();
        }
        String ids = String.join(",", symbols.stream().map(String::toLowerCase).toList());
        String vs = fiat.toLowerCase();

        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=" + vs;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return response != null ? response : Map.of();
    }
}
