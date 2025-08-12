package app.cryptarch.controller;

import app.cryptarch.dto.NewsArticleDto;
import app.cryptarch.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsApiService newsApiService;

    // Example: http://localhost:8081/api/news?query=bitcoin&from=2025-08-01T00:00:00Z&to=2025-08-12T00:00:00Z
    @GetMapping
    public ResponseEntity<?> getNews(
            @RequestParam String query,
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            ZonedDateTime fromDate = ZonedDateTime.parse(from);
            ZonedDateTime toDate = ZonedDateTime.parse(to);

            List<Map<String, Object>> rawArticles =
                    newsApiService.fetchNewsByDateAndKeywords(query, fromDate, toDate);

            List<NewsArticleDto> articles = rawArticles.stream()
                    .map(a -> new NewsArticleDto(
                            (String) a.get("title"),
                            (String) a.get("url"),
                            a.get("source") instanceof Map<?, ?> s
                                    ? String.valueOf(((Map<?, ?>) s).get("name"))
                                    : null,
                            parseDate((String) a.get("publishedAt"))
                    ))
                    .toList();

            if (articles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No articles found"));
            }

            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid request format: " + e.getMessage()));
        }
    }

    private ZonedDateTime parseDate(String dateString) {
        try {
            return ZonedDateTime.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}