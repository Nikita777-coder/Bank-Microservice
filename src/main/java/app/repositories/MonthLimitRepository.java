package app.repositories;

import app.entities.MonthLimitEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonthLimitRepository extends CrudRepository<MonthLimitEntity, UUID> {
    List<MonthLimitEntity> findAllByClientIdAndTimestampMonthValueOrderByTimestampDesc(UUID clientId, int month);
}
