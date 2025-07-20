package app.cryptarch.service;

import app.cryptarch.dto.CryptoRequest;
import app.cryptarch.dto.CryptoResponse;

import java.util.List;

public interface CryptoService {
    CryptoResponse create(CryptoRequest createRequest);
    List<CryptoResponse> findAll();
    CryptoResponse getById(Long id);
}
