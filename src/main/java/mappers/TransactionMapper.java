package mappers;

import dto.ConsumableOperation;
import dto.TransactionDTO;
import entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MonthLimitMapper.class)
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    TransactionEntity consumableOperationToTransactionEntity(ConsumableOperation request);
    List<TransactionDTO> transactionEntitiesToTransactionDTOs(List<TransactionEntity> transactionEntities);
}
