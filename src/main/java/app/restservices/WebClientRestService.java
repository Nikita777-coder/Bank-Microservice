package app.restservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientRestService implements RestService {
    private final WebClient webClient;
    @Override
    public <T> T get(String uri, Class<T> typeKey) {
        Mono<T> response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(uri).build())
                .retrieve()
                .bodyToMono(typeKey);

        return response.block();
    }
}
