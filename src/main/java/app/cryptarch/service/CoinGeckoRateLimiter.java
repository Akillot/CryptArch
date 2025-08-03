package app.cryptarch.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CoinGeckoRateLimiter {

    private final Bucket bucket;

    public CoinGeckoRateLimiter() {
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(){
        return bucket.tryConsume(1);
    }
}
