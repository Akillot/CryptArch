package app.cryptarch.controller;

import app.cryptarch.dto.CryptoRequest;
import app.cryptarch.dto.CryptoResponse;
import app.cryptarch.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cryptos")
public class CryptoController {

    private final CryptoService cryptoService;

    @Autowired
    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping
    public CryptoResponse create(@RequestBody CryptoRequest cryptoRequest) {
        return cryptoService.create(cryptoRequest);
    }

    @GetMapping
    public List<CryptoResponse> findAll() {
        return cryptoService.findAll();
    }

    @GetMapping("/{id}")
    public CryptoResponse findById(@PathVariable Long id) {
        return cryptoService.findById(id);
    }
}
