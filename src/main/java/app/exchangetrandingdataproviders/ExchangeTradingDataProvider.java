package app.exchangetrandingdataproviders;

import app.dto.RateExchangeProviderResponse;
import app.dto.ratexchange.RateExchangeRequest;

public interface ExchangeTradingDataProvider {
    RateExchangeProviderResponse getRateExchange(RateExchangeRequest request);
}
