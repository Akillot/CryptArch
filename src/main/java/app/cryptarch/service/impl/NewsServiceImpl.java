package app.cryptarch.service.impl;

import app.cryptarch.dto.NewsResponse;
import app.cryptarch.entity.News;
import app.cryptarch.repo.NewsRepo;
import app.cryptarch.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepo newsRepo;

    @Override
    public List<NewsResponse> getNewsByCryptoIdAndDateRange(Long cryptoId, ZonedDateTime from, ZonedDateTime to) {
        List<News> newsList = newsRepo.findByCryptoIdAndPublishedAtBetween(cryptoId, from, to);
        return newsList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NewsResponse toResponse(News news) {
        NewsResponse dto = new NewsResponse();
        dto.setId(news.getId());
        dto.setCryptoId(news.getCrypto().getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setSource(news.getSource());
        dto.setUrl(news.getUrl());
        dto.setPublishedAt(news.getPublishedAt());
        return dto;
    }
}
