package mappers;

import dto.RateExchangeProviderResponse;
import dto.ratexchange.RateExchangeResponse;
import entities.RateExchangeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RateExchangeMapper {
    @Mapping(source = "to", target = "currency")
    RateExchangeResponse rateExchangeEntityToRateExchangeResponse(RateExchangeEntity entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "from", ignore = true)
    @Mapping(source = "symbol", target = "to")
    @Mapping(source = "timestamp", target = "fixedDate")
    RateExchangeEntity rateExchangeProviderResponseToRateExchangeEntity(RateExchangeProviderResponse response);
}
