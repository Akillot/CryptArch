package app.cryptarch.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface NewsApiService {
    List<Map<String, Object>> fetchNewsByDateAndKeywords(String keyword, ZonedDateTime from, ZonedDateTime to);
}
