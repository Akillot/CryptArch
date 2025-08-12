package app.cryptarch.dto;

import java.time.ZonedDateTime;

public record NewsArticleDto(
        String title,
        String url,
        String source,
        ZonedDateTime publishedAt
) {}
