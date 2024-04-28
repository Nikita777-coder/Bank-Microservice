package app.controllers;

import app.dto.ConsumableOperation;
import app.dto.GetLimitAns;
import app.dto.limit.MonthLimitRequest;
import app.enums.TransferType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientControllerTest {
    private static WebTestClient webTestClient;
    @BeforeAll
    static void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }
    @Test
    @DisplayName("getLimitTransactions test; give 3 operations with amount=1000$; must return two last operations with month balance=1000$")
    void getLimitTransactions() {
        var clientId = UUID.randomUUID();

        ConsumableOperation consumableOperation = new ConsumableOperation(
                clientId,
                "USD",
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                TransferType.GOODS
        );

        for (int i = 0; i < 3; ++i) {
            webTestClient.post().uri("/transactions/save-consumable-operation")
                    .bodyValue(consumableOperation)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is2xxSuccessful();
        }

        webTestClient.get().uri("/client/transactions")
                .attribute("clientId", clientId)
                .attribute("transferType", TransferType.GOODS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetLimitAns.class)
                .consumeWith(result ->
                        {
                            assertEquals(2, result.getResponseBody().getBody().size());
                            assertEquals(BigDecimal.valueOf(1000), result.getResponseBody().getBody().get(1).getLimit().getLimit());
                        }
                );
    }

    @Test
    @DisplayName("setNewLimit test; give 2 operations with amount=1000$, set new limit=5000$ to client which makes operations, give  2 operations with amount=1000$; limit transactions list must be consist of operation from month limit=1000$")
    void setNewLimit() {
        var clientId = UUID.randomUUID();

        ConsumableOperation consumableOperation = new ConsumableOperation(
                clientId,
                "USD",
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                TransferType.GOODS
        );

        for (int i = 0; i < 2; ++i) {
            webTestClient.post().uri("/transactions/save-consumable-operation")
                    .bodyValue(consumableOperation)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is2xxSuccessful();
        }

        MonthLimitRequest monthLimitRequest = MonthLimitRequest.builder().
                limit(BigDecimal.valueOf(5000))
                .currency("USD")
                .clientId(clientId)
                .build();

        webTestClient.post().uri("client/new-limit")
                .bodyValue(monthLimitRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        webTestClient.get().uri("/client/transactions")
                .attribute("clientId", clientId)
                .attribute("transferType", TransferType.GOODS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetLimitAns.class)
                .consumeWith(result ->
                        {
                            assertEquals(1, result.getResponseBody().getBody().size());
                            assertEquals(BigDecimal.valueOf(1000), result.getResponseBody().getBody().get(1).getLimit().getLimit());
                        }
                );
    }
}