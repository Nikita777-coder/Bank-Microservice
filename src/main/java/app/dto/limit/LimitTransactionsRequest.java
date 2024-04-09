package app.dto.limit;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LimitTransactionsRequest {
    private UUID clientId;
    private String transferType;
}
