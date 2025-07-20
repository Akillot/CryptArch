package app.cryptarch.service;

import app.cryptarch.dto.CryptoRequest;
import app.cryptarch.dto.CryptoResponse;
import app.cryptarch.entity.Crypto;
import app.cryptarch.repo.CryptoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepo cryptoRepo;

    @Autowired
    public CryptoServiceImpl(CryptoRepo cryptoRepo) {
        this.cryptoRepo = cryptoRepo;
    }

    @Override
    public CryptoResponse create(CryptoRequest createRequest) {
        Crypto crypto = new Crypto();
        crypto.setName(createRequest.getName());
        crypto.setSymbol(createRequest.getSymbol());

        Crypto savedCrypto = cryptoRepo.save(crypto);
        return toResponse(savedCrypto);
    }

    @Override
    public List<CryptoResponse> findAll() {
        return cryptoRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CryptoResponse getById(Long id) {
       Crypto crypto = cryptoRepo.findById(id)
               .orElseThrow(() -> new RuntimeException("Crypto not found"));

       return toResponse(crypto);
    }

    private CryptoResponse toResponse(Crypto crypto) {
        CryptoResponse response = new CryptoResponse();
        response.setId(crypto.getId());
        response.setName(crypto.getName());
        response.setSymbol(crypto.getSymbol());
        return response;
    }
}
