package entities;

import enums.TransferType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "month_limits",
    indexes = {@Index(name = "clientIdIndex", columnList = "clientId"),
            @Index(name = "timestampIndex", columnList = "timestamp", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthLimitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "limit_number")
    private BigDecimal limitNumber;

    @Column(name = "limit_balance")
    private BigDecimal limitBalance;

    private String currency;

    @Column(name = "type_of_month_limit")
    private TransferType monthLimitType;

    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "limit",
            cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<TransactionEntity> transactions;
}
