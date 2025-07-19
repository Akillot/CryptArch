package app.cryptarch.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class NewsRequest {
    private Long cryptoId;
    private String title;
    private String content;
    private ZonedDateTime publishedAt;
    private String source;
    private String url;
}
