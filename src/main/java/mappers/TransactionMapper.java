package mappers;

import dto.ConsumableOperation;
import entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    TransactionEntity consumableOperationToTransactionEntity(ConsumableOperation request);
}
