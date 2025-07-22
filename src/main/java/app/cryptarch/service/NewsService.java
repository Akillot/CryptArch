package app.cryptarch.service;

import app.cryptarch.dto.NewsResponse;

import java.time.ZonedDateTime;
import java.util.List;

public interface NewsService {
    List<NewsResponse> getNewsByCryptoIdAndDateRange(Long cryptoId, ZonedDateTime from, ZonedDateTime to);
}