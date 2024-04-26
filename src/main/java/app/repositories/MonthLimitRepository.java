package app.repositories;

import app.entities.MonthLimitEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonthLimitRepository extends CrudRepository<MonthLimitEntity, UUID> {
    @Query("select mlr from MonthLimitEntity mlr where mlr.clientId=?1 and extract(month from mlr.timestamp)=?2 order by mlr.timestamp desc")
    List<MonthLimitEntity> getAllByClientIdAndTimestampMonthValueOrderByTimestampDesc(UUID clientId, int month);
}
