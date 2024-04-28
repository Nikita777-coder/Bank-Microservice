package app.dto.limit;

import app.enums.TransferType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LimitTransactionsRequest {
    @NotBlank(message = "client id is required!")
    private UUID clientId;
    private TransferType transferType;
}
