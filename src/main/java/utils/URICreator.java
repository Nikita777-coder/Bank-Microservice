package utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@UtilityClass
public class URICreator {
    private static final UriBuilder URI_BUILDER = new DefaultUriBuilderFactory().builder();
    public static String formRateExchangeRequest(String path, Object fromParam, Object toParam, Object apiKey) {
        return URI_BUILDER
                .path(path)
                .queryParam("symbol", String.format("%s/%s", fromParam, toParam))
                .queryParam("apikey", apiKey)
                .build()
                .toString();
    }
}