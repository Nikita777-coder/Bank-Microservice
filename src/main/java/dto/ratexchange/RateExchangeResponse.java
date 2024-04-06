package dto.ratexchange;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RateExchangeResponse {
    private String currency;
    private BigDecimal rate;
}
