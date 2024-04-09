package app.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RateExchangeProviderResponse {
    private String symbol;
    private BigDecimal rate;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
