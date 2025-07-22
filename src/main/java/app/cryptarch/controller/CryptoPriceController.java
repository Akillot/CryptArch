package app.cryptarch.controller;

import app.cryptarch.dto.CryptoPriceResponse;
import app.cryptarch.service.CryptoPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class CryptoPriceController {

    private final CryptoPriceService cryptoPriceService;

    @GetMapping("/{cryptoId}")
    public List<CryptoPriceResponse> getPricesByCrypto(@PathVariable Long cryptoId) {
        return cryptoPriceService.getPricesByCryptoId(cryptoId);
    }
}
