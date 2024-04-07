package services;

import dto.TransactionDTO;
import dto.limit.LimitTransactionsRequest;
import dto.limit.MonthLimitRequest;
import entities.MonthLimitEntity;
import entities.TransactionEntity;
import enums.TransferType;
import lombok.RequiredArgsConstructor;
import mappers.MonthLimitMapper;
import mappers.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import repositories.MonthLimitRepository;
import repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final MonthLimitMapper monthLimitMapper;
    private final MonthLimitRepository monthLimitRepository;
    public List<TransactionDTO> getLimitTransactions(LimitTransactionsRequest request) {
        TransferType requestTransactionType = monthLimitMapper.stringToTransferType(request.getTransferType());
        return transactionMapper.transactionEntitiesToTransactionDTOs(transactionRepository.
                findAllByClientIdAndTransferTypeAndLimitExceeded(request.getClientId(), requestTransactionType, false)
        );
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UUID setNewLimit(MonthLimitRequest monthLimitRequest) {
        TransferType transferType = monthLimitMapper.stringToTransferType(monthLimitRequest.getTransactionType());
        List<TransactionEntity> foundTransactions = transactionRepository
                .findAllByClientIdAndTransferTypeAndTimeOperationMonthValue(
                        monthLimitRequest.getClientId(),
                        transferType,
                        LocalDateTime.now().getMonth().getValue()
                );

        MonthLimitEntity monthLimit = monthLimitMapper.monthLimitRequestToMonthLimitEntity(monthLimitRequest);

        for (var transaction : foundTransactions) {
            monthLimit.setLimitBalance(monthLimit.getLimitBalance().subtract(transaction.getAmount()));
        }

        return monthLimitRepository.save(monthLimit).getId();
    }
}
