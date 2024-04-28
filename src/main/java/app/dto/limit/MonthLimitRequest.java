package app.dto.limit;

import app.enums.TransferType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MonthLimitRequest {
    @NotBlank(message = "clientId required!")
    private UUID clientId;
    private String currency;
    private BigDecimal limit;
    private TransferType transactionType = TransferType.SERVICES;
}
