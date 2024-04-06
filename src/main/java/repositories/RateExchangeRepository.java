package repositories;

import entities.RateExchangeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateExchangeRepository extends CrudRepository<RateExchangeEntity, UUID> {
    Optional<RateExchangeEntity> findByFromAndTo(String from, String to);
}
