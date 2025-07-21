package app.cryptarch.controller;

import app.cryptarch.dto.CryptoPriceResponse;
import app.cryptarch.dto.CryptoRequest;
import app.cryptarch.dto.CryptoResponse;
import app.cryptarch.service.CryptoPriceService;
import app.cryptarch.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class CryptoPriceController {

    private final CryptoPriceService cryptoPriceService;

    @PostMapping
    public CryptoResponse create(@RequestBody CryptoRequest cryptoRequest){
        return cryptoPriceService.create(cryptoRequest);
    }

    @GetMapping("/crypto/{cryptoId}")
    public List<CryptoPriceResponse> getPricesByCryptoId(@PathVariable Long cryptoId) {
        return cryptoPriceService.getPricesByCryptoId(cryptoId);
    }
}
