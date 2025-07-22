package app.cryptarch.service.impl;

import app.cryptarch.dto.CryptoPriceResponse;
import app.cryptarch.entity.CryptoPrice;
import app.cryptarch.repo.CryptoPriceRepo;
import app.cryptarch.service.CryptoPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoPriceServiceImpl implements CryptoPriceService {

    private final CryptoPriceRepo cryptoPriceRepo;

    @Override
    public List<CryptoPriceResponse> getPricesByCryptoId(Long cryptoId) {
        return cryptoPriceRepo.findByCryptoId(cryptoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CryptoPriceResponse mapToResponse(CryptoPrice entity) {
        CryptoPriceResponse dto = new CryptoPriceResponse();
        dto.setId(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setPriceTime(entity.getPriceTime());
        dto.setCryptoId(entity.getCrypto().getId());
        return dto;
    }
}
