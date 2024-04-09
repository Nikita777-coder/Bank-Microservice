package app.exchangetrandingdataproviders;

import app.dto.RateExchangeProviderResponse;
import app.dto.ratexchange.RateExchangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import app.restservices.RestService;
import app.utils.URICreator;

@Component
@RequiredArgsConstructor
public class TwelveExchangeTradingDataProvider implements ExchangeTradingDataProvider {
    @Qualifier("webClientRestService")
    private final RestService restService;

    @Value("${server-configs.exchange-trading-data-provider.twelve.base-url}")
    private String baseUrl;

    @Value("${server-configs.exchange-trading-data-provider.twelve.rate-exchange-path}")
    private String rateExchangeHTTPRoute;

    @Value("${server-configs.exchange-trading-data-provider.twelve.api-key}")
    private String apiKey;

    @Override
    public RateExchangeProviderResponse getRateExchange(RateExchangeRequest request) {
        return restService.get(URICreator.formRateExchangeRequest(
                    baseUrl + rateExchangeHTTPRoute,
                        request.getFrom(),
                        request.getTo(),
                        apiKey
                ),
                RateExchangeProviderResponse.class
        );
    }
}
