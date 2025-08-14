package app.cryptarch.service;

import app.cryptarch.dto.NewsArticleDto;
import app.cryptarch.dto.OverviewDto;
import app.cryptarch.dto.PriceDto;
import app.cryptarch.service.impl.CryptoApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OverviewService {

    private final CryptoApiServiceImpl cryptoApi;
    private final NewsApiService newsApi;
    private final CoinGeckoRateLimiter coinGeckoLimiter;

    public OverviewDto getOverview(String cryptoId, String fiat, int days) {
        String id = cryptoId.toLowerCase();
        String vs = fiat.toLowerCase();

        if (!coinGeckoLimiter.tryConsume()) {
            throw new RateLimitException("CoinGecko rate limit reached");
        }

        Map<String,Object> priceMap = cryptoApi.fetchPriceFromApi(id, vs);
        double price = extractPrice(priceMap, id, vs);

        ZonedDateTime to = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime from = to.minusDays(days);
        List<Map<String,Object>> raw = newsApi.fetchNewsByDateAndKeywords(id, from, to);
        List<NewsArticleDto> news = raw.stream().map(a ->
                new NewsArticleDto(
                        (String) a.get("title"),
                        (String) a.get("url"),
                        a.get("source") instanceof Map<?,?> s ? String.valueOf(((Map<?,?>) s).get("name")) : null,
                        parseIso((String) a.get("publishedAt"))
                )
        ).toList();

        return new OverviewDto(new PriceDto(id, vs, price), news);
    }

    private static double extractPrice(Map<String,Object> data, String id, String vs) {
        if (data == null || data.isEmpty()) throw new NotFoundException("No price data for " + id + " in " + vs);
        Object o = data.get(id);
        if (!(o instanceof Map<?,?> m) || !m.containsKey(vs)) {
            throw new NotFoundException("No price data for " + id + " in " + vs);
        }
        Object p = m.get(vs);
        return (p instanceof Number n) ? n.doubleValue() : 0.0;
    }

    private static ZonedDateTime parseIso(String s) {
        try { return ZonedDateTime.parse(s); } catch (Exception e) { return null; }
    }

    public static class NotFoundException extends RuntimeException { public NotFoundException(String m){ super(m);} }
    public static class RateLimitException extends RuntimeException { public RateLimitException(String m){ super(m);} }
}
