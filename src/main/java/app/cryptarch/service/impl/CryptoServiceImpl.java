package app.cryptarch.service.impl;

import app.cryptarch.dto.CryptoRequest;
import app.cryptarch.dto.CryptoResponse;
import app.cryptarch.entity.Crypto;
import app.cryptarch.repo.CryptoRepo;
import app.cryptarch.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepo cryptoRepo;

    @Override
    public List<CryptoResponse> getAll() {
        return cryptoRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CryptoResponse getById(Long id) {
        return cryptoRepo.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public CryptoResponse create(CryptoRequest request) {
        Crypto crypto = new Crypto();
        crypto.setSymbol(request.getSymbol());
        crypto.setName(request.getName());
        return toResponse(cryptoRepo.save(crypto));
    }

    private CryptoResponse toResponse(Crypto crypto) {
        CryptoResponse response = new CryptoResponse();
        response.setId(crypto.getId());
        response.setName(crypto.getName());
        response.setSymbol(crypto.getSymbol());
        return response;
    }
}