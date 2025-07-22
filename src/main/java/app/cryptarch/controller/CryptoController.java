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
    public CryptoResponse create(@RequestBody CryptoRequest request) {
        return cryptoService.create(request);
    }

    @GetMapping
    public List<CryptoResponse> getAll() {
        return cryptoService.getAll();
    }

    @GetMapping("/{id}")
    public CryptoResponse getById(@PathVariable Long id) {
        return cryptoService.getById(id);
    }
}
