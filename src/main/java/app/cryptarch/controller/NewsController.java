package app.cryptarch.controller;

import app.cryptarch.dto.NewsResponse;
import app.cryptarch.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{cryptoId}")
    public List<NewsResponse> getNewsByCryptoAndDateRange(
            @PathVariable Long cryptoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to
    ) {
        return newsService.getNewsByCryptoIdAndDateRange(cryptoId, from, to);
    }
}
