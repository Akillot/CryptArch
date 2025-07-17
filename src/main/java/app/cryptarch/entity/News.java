package app.cryptarch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id")
    private Crypto crypto;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "published_at")
    private ZonedDateTime publishedAt;

    private String source;

    @Column(columnDefinition = "text")
    private String url;
}
