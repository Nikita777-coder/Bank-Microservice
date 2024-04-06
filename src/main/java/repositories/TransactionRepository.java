package repositories;

import entities.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID> {
}
