package app.mappers;

import app.dto.limit.MonthLimitRequest;
import app.dto.limit.MonthLimitResponse;
import app.entities.MonthLimitEntity;
import app.enums.TransferType;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MonthLimitMapper {
    @Mapping(source = "limit", target = "limitNumber")
    @Mapping(source = "limit", target = "limitBalance")
    MonthLimitEntity monthLimitRequestToMonthLimitEntity(MonthLimitRequest request);
    TransferType stringToTransferType(String request);
    @Mapping(source = "limitNumber", target = "limit")
    @Mapping(source = "timestamp", target = "setTime")
    MonthLimitResponse monthLimitEntityToMonthLimitResponse(MonthLimitEntity entity);
    @BeforeMapping
    default void stringToUp(String request) {
        request = request.toUpperCase();
    }
}
