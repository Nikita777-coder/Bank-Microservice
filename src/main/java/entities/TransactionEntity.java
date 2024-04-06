package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "transactions")
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

    @Column(name = "limit_exceeded")
    private Boolean limitExceeded;

    @Column(name = "time_operation")
    private LocalDateTime timeOperation;
}
