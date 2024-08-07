package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "rate_exchanges",
    indexes = {@Index(name = "fromCurrencyIndex", columnList = "fromCurrencyIndex"), @Index(name = "toCurrencyIndex", columnList = "toCurrencyIndex")})
@Entity
@NoArgsConstructor
@Getter
@Setter
public class RateExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fromCurrencyIndex")
    private String from;

    @Column(name = "toCurrencyIndex")
    private String to;
    private BigDecimal rate;

    @Column(name = "fixed_date", updatable = false)
    private LocalDateTime fixedDate;
}
