package exchangetrandingdataproviders;

import dto.RateExchangeProviderResponse;
import dto.ratexchange.RateExchangeRequest;

public interface ExchangeTradingDataProvider {
    RateExchangeProviderResponse getRateExchange(RateExchangeRequest request);
}
