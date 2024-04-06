package services;

import dto.ConsumableOperation;
import dto.ratexchange.RateExchangeRequest;
import dto.ratexchange.RateExchangeResponse;
import entities.RateExchangeEntity;
import entities.TransactionEntity;
import exchangetrandingdataproviders.ExchangeTradingDataProvider;
import lombok.RequiredArgsConstructor;
import mappers.RateExchangeMapper;
import mappers.TransactionMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import repositories.RateExchangeRepository;
import repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final RateExchangeMapper rateExchangeMapper;
    private final MonthLimitService monthLimitService;
    private final RateExchangeRepository rateExchangeRepository;

    @Qualifier("twelveExchangeTradingDataProvider")
    private final ExchangeTradingDataProvider rateProvider;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UUID saveConsumableOperation(ConsumableOperation consumableOperation) {
        Month operationMonth = consumableOperation.getOperationTime().getMonth();
        MonthLimitEntity monthLimit = getMonthLimit(consumableOperation.getClientId(), operationMonth);
        TransactionEntity transaction = transactionMapper.consumableOperationToTransactionEntity(consumableOperation);

        monthLimit.setLimitBalance(monthLimit.getLimitBalance().subtract(consumableOperation.getAmount()));
        monthLimitService.updateMonthLimitTable(monthLimit);
        transaction.setLimitExceeded(monthLimit.getLimitBalance().compareTo(BigDecimal.ZERO) < 0);

        return transactionRepository.save(transaction).getId();
    }
    public RateExchangeResponse getStockExchangeData(RateExchangeRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<RateExchangeEntity> foundRateExchange = rateExchangeRepository.findByFromAndTo(
                request.getFrom(), request.getTo()
        );
        if (foundRateExchange.isEmpty()) {
            return saveNewRateExchange(request);
        }

        RateExchangeEntity foundEntity = foundRateExchange.get();
        if (currentTime.isBefore(foundEntity.getFixedDate())) {
            saveNewRateExchange(request, foundEntity.getId());
        }

        return rateExchangeMapper.rateExchangeEntityToRateExchangeResponse(foundEntity);
    }
    private RateExchangeResponse saveNewRateExchange(RateExchangeRequest request) {
        return saveRateExchangeEntity(generateBaseRateExchangeEntity(request));
    }
    private RateExchangeResponse saveNewRateExchange(RateExchangeRequest request, UUID rateExchangeEntityId) {
        RateExchangeEntity newRateExchange = generateBaseRateExchangeEntity(request);
        newRateExchange.setId(rateExchangeEntityId);

        return saveRateExchangeEntity(newRateExchange);
    }
    private RateExchangeEntity generateBaseRateExchangeEntity(RateExchangeRequest request) {
        RateExchangeEntity newRateExchange = rateExchangeMapper.rateExchangeProviderResponseToRateExchangeEntity
                (rateProvider.getRateExchange(request));
        newRateExchange.setFrom(request.getFrom());

        return newRateExchange;
    }
    private RateExchangeResponse saveRateExchangeEntity(RateExchangeEntity rateExchangeEntity) {
        return rateExchangeMapper.rateExchangeEntityToRateExchangeResponse(rateExchangeRepository.save(rateExchangeEntity));
    }
    private MonthLimitEntity getMonthLimit(UUID clientId, Month month) {
        Optional<MonthLimitEntity> foundLimit = monthLimitService.findLastMonthLimitByClientIdAndMonth(
                clientId,
                month
        );

        if (foundLimit.isEmpty()) {
            return monthLimitService.createDefaultMonthLimit(clientId);
        }

        return foundLimit.get();
    }
}
