package app.cryptarch.repo;

import app.cryptarch.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoPriceRepo extends JpaRepository<CryptoPrice, Long> {}
