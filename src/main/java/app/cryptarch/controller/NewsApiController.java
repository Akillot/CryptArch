package app.cryptarch.controller;

import app.cryptarch.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external/news")
@RequiredArgsConstructor
public class NewsApiController {

    private final NewsApiService newsApiService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getNewsFromApi(
            @RequestParam String keyword,
            @RequestParam String from,
            @RequestParam String to
    ) {
        ZonedDateTime fromDate = ZonedDateTime.parse(from);
        ZonedDateTime toDate = ZonedDateTime.parse(to);

        List<Map<String, Object>> news = newsApiService.fetchNewsByDateAndKeywords(keyword, fromDate, toDate);

        if (news.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(news);
    }
}
