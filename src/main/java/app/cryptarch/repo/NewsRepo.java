package app.cryptarch.repo;

import app.cryptarch.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface NewsRepo extends JpaRepository<News, Long> {
    List<News> findByCryptoIdAndPublishedAtBetween(Long cryptoId, ZonedDateTime start, ZonedDateTime end);
}
