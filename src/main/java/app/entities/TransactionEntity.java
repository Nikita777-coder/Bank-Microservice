package app.entities;

import app.enums.TransferType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "transactions",
    indexes = {
            @Index(name = "transferTypeIndex", columnList = "transferType"),
            @Index(name = "timeOperationIndex", columnList = "timeOperation", unique = true)
    }
)
@Entity
@NoArgsConstructor
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;
    private String currency;
    private BigDecimal amount;

    @Column(name = "transfer_type")
    private TransferType transferType;

    @Column(name = "limit_exceeded")
    private Boolean limitExceeded;

    @Column(name = "time_operation", updatable = false)
    private LocalDateTime timeOperation;

    @ManyToOne
    @JoinColumn(name = "client_limit")
    private MonthLimitEntity limit;
}
