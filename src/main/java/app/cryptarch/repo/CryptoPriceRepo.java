package app.cryptarch.repo;

import app.cryptarch.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoPriceRepo extends JpaRepository<CryptoPrice, Long> {
    List<CryptoPrice> findByCryptoId(Long cryptoId);
}
