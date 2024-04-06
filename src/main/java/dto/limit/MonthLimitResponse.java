package dto.limit;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MonthLimitResponse {
    private String currency;
    private BigDecimal limit;
    private LocalDateTime setTime;
}
