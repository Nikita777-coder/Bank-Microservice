package dto;

import enums.TransferType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConsumableOperation {
    @NotBlank(message = "clientId required!")
    private UUID clientId;
    private String currency;
    private BigDecimal amount;
    private LocalDateTime operationTime;
    private TransferType transferType;
}
