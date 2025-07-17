package app.cryptarch.repo;

import app.cryptarch.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepo extends JpaRepository<Crypto, Long> {}
