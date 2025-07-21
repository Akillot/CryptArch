package app.cryptarch.service;

import app.cryptarch.dto.CryptoPriceResponse;

import java.util.List;

public interface CryptoPriceService {
    List<CryptoPriceResponse> getPriceByCryptoId(Long cryptoId);
}
