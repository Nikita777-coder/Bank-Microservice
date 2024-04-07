package services;

import entities.MonthLimitEntity;
import enums.TransferType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repositories.MonthLimitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonthLimitService {
    private final MonthLimitRepository monthLimitRepository;
    @Value("${server-configs.month-limit.defaultLimitNumber}")
    private BigDecimal defaultLimitNumber;
    public Optional<MonthLimitEntity> findLastMonthLimitByClientIdAndMonth(UUID clientId, Month month) {
        return monthLimitRepository.findAllByClientIdAndTimestampMonthValueOrderByTimestampDesc(
                clientId,
                month.getValue()
        ).stream().findFirst();
    }
    public MonthLimitEntity createDefaultMonthLimit(UUID clientId) {
        return MonthLimitEntity
                    .builder()
                    .clientId(clientId)
                    .monthLimitType(TransferType.SERVICES)
                    .timestamp(LocalDateTime.now())
                    .limitNumber(defaultLimitNumber)
                    .limitBalance(defaultLimitNumber)
                    .build();
    }
    public MonthLimitEntity updateMonthLimitTable(MonthLimitEntity monthLimitEntity) {
        return monthLimitRepository.save(monthLimitEntity);
    }
}
