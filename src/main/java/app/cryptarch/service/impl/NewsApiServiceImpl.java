package app.cryptarch.service.impl;

import app.cryptarch.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewsApiServiceImpl implements NewsApiService {

    @Value("${newsapi.key}")
    private String newsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Map<String, Object>> fetchNewsByDateAndKeywords(String keyword, ZonedDateTime from, ZonedDateTime to) {
        String url = String.format(
                "https://newsapi.org/v2/everything?q=%s&from=%s&to=%s&apiKey=%s",
                keyword,
                from.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                to.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                newsApiKey
        );

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if(response == null || !response.containsKey("articles")) {
            return List.of();
        }

        return (List<Map<String, Object>>) response.get("articles");
    }
}
