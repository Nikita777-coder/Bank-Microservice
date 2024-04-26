package app.repositories;

import app.entities.TransactionEntity;
import app.enums.TransferType;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT tr from TransactionEntity tr where tr.clientId=?1 and tr.transferType=?2 and extract(month from tr.timeOperation)=?3")
    List<TransactionEntity> getAllByClientIdAndTransferTypeAndTimeOperationMonthValue(UUID clientId,
                                                                                       TransferType transferType,
                                                                                       int month);
}
