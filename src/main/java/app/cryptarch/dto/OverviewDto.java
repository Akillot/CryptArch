package app.cryptarch.dto;

import java.util.List;

public record OverviewDto(
        PriceDto price,
        List<NewsArticleDto> news
) {}
