package app.cryptarch.controller;

import app.cryptarch.service.NewsApiService;
import lombok.RequiredArgsConstructor;
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
    public List<Map<String, Object>> getNewsFromApi(
            @RequestParam String keyword,
            @RequestParam ZonedDateTime from,
            @RequestParam ZonedDateTime to
    ) {
        return newsApiService.fetchNewsByDateAndKeywords(keyword, from, to);
    }
}
