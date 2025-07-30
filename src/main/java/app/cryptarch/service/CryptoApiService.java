package app.cryptarch.service;

import java.util.Map;

public interface CryptoApiService {
    Map<String, Object> fetchPriceFromApi(String symbol);
}
