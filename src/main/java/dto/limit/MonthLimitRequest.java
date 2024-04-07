package dto.limit;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MonthLimitRequest {
    @NotBlank(message = "clientId required!")
    private UUID clientId;
    private String currency;
    private BigDecimal limit;
    private String transactionType = "service";
}
