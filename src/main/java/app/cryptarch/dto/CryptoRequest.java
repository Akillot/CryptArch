package app.cryptarch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CryptoRequest {
    private String symbol;
    private String name;
}
