package repositories;

import entities.TransactionEntity;
import enums.TransferType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByClientIdAndTransferTypeAndLimitExceeded(UUID clientId,
                                                                             TransferType transferType,
                                                                             boolean limitExceeded
    );
    List<TransactionEntity> findAllByClientIdAndTransferTypeAndTimeOperationMonthValue(UUID clientId,
                                                                                       TransferType transferType,
                                                                                       int month);
}
