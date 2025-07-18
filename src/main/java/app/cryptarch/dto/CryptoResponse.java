package app.cryptarch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoResponse {
    private Long id;
    private String symbol;
    private String name;
}
