package app.cryptarch.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class CryptoPriceResponse {
    private Long id;
    private BigDecimal price;
    private ZonedDateTime priceTime;
    private Long cryptoId;
}
