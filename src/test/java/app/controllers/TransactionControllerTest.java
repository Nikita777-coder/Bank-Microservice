package app.controllers;

import app.dto.ConsumableOperation;
import app.dto.GetLimitAns;
import app.dto.TransactionDTO;
import app.dto.ratexchange.RateExchangeRequest;
import app.dto.ratexchange.RateExchangeResponse;
import app.enums.TransferType;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionControllerTest {
    private static WebTestClient webTestClient;

    @Mock
    private LocalDateTime localDateTimeMock;

    @BeforeAll
    static void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }
    @Test
    @DisplayName("getStockExchangeData test; give RateExchangeRequest(from=KZT, to=USD); must return RateExchangeResponse(currency=USD, amount=<current amount from stock server>)")
    void getStockExchangeData1() {
        RateExchangeRequest request = new RateExchangeRequest("KZT", "USD");

        webTestClient
                .get()
                .uri("transactions/rate-exchange")
                .attribute("from", request.getFrom())
                .attribute("to", request.getTo())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RateExchangeResponse.class)
                .consumeWith(result ->
                        {
                            assertEquals("USD", Objects.requireNonNull(result.getResponseBody()).getCurrency());
                            assertNotNull(result.getResponseBody().getRate());
                        }
                );
    }
    @Test
    @DisplayName("getStockExchangeData test 2; give RateExchangeRequest(from=KZT, to=USD) twice; second call must be same to first")
    void getStockExchangeData2() {
        RateExchangeRequest request = new RateExchangeRequest("KZT", "USD");

        doReturn(LocalDateTime.of(2030,01,01,22,22,22)).when(localDateTimeMock).now();

        var call1 = webTestClient
                .get()
                .uri("/transactions/rate-exchange")
                .attribute("from", request.getFrom())
                .attribute("to", request.getTo())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RateExchangeResponse.class)
                .consumeWith(result ->
                        {
                            assertEquals("USD", Objects.requireNonNull(result.getResponseBody()).getCurrency());
                            assertNotNull(result.getResponseBody().getRate());
                        }
                )
                .returnResult()
                .getResponseBody();

        webTestClient
                .get()
                .uri("/transactions/rate-exchange")
                .accept(MediaType.APPLICATION_JSON)
                .attribute("from", request.getFrom())
                .attribute("to", request.getTo())
                .exchange()
                .expectStatus().isOk()
                .expectBody(RateExchangeResponse.class)
                .consumeWith(result ->
                        {
                            assertEquals(Objects.requireNonNull(call1).getCurrency(), Objects.requireNonNull(result.getResponseBody()).getCurrency());
                            assertEquals(Objects.requireNonNull(call1).getRate(), Objects.requireNonNull(result.getResponseBody()).getRate());
                        }
                );
    }
    @Test
    @DisplayName("getStockExchangeData test 3; give RateExchangeRequest(from=KZT, to=USD) twice but fixed date of first is expired; must return new rate and fixed date")
    void getStockExchangeData3() {
        RateExchangeRequest request = new RateExchangeRequest("KZT", "USD");

        var call1 = webTestClient
                .get()
                .uri("/transactions/rate-exchange")
                .attribute("from", request.getFrom())
                .attribute("to", request.getTo())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RateExchangeResponse.class)
                .consumeWith(result ->
                        {
                            assertEquals("USD", Objects.requireNonNull(result.getResponseBody()).getCurrency());
                            assertNotNull(result.getResponseBody().getRate());
                        }
                )
                .returnResult()
                .getResponseBody();

        doReturn(LocalDateTime.of(1999,11,11,22,22,22)).when(localDateTimeMock).now();
        webTestClient
                .get()
                .uri("/transactions/rate-exchange")
                .accept(MediaType.APPLICATION_JSON)
                .attribute("from", request.getFrom())
                .attribute("to", request.getTo())
                .exchange()
                .expectStatus().isOk()
                .expectBody(RateExchangeResponse.class)
                .consumeWith(result ->
                        assertNotEquals(Objects.requireNonNull(call1).getRate(), Objects.requireNonNull(result.getResponseBody()).getRate())
                );
    }

    /**
     * save operation unit tests
     * 1. Save simple operation with amount=100; must return operation with not limited flag and month balance must be 900
     * 2. Save two operatoins. first balance 1000, another 200. Month balance must be -200,and firs transaction
     * must be ok but second must be with limit flag
     */
    @Test
    @DisplayName("saveConsumableOperation test 1; Save simple operation with amount=100; must return operation with not limited flag and month balance must be 900")
    void saveConsumableOperationTest1() {
        ConsumableOperation consumableOperation = new ConsumableOperation(
                UUID.randomUUID(),
                "USD",
                BigDecimal.valueOf(1234567),
                LocalDateTime.now(),
                TransferType.GOODS
        );

        webTestClient.post().uri("/transactions/save-consumable-operation")
                .bodyValue(consumableOperation)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UUID.class)
                .consumeWith(Assertions::assertNotNull);
    }
    @Test
    @DisplayName("saveConsumableOperation test 2; Save two operations. First with amount=1000, second amount=200; get limit transactions list must return second transaction and month limit must be -200")
    void saveConsumableOperationTest2() {
        var userId = UUID.randomUUID();
        ConsumableOperation consumableOperation1 = new ConsumableOperation(
                userId,
                "USD",
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                TransferType.GOODS
        );

        ConsumableOperation consumableOperation2 = new ConsumableOperation(
                userId,
                "USD",
                BigDecimal.valueOf(200),
                LocalDateTime.now(),
                TransferType.GOODS
        );

        webTestClient.post().uri("/transactions/save-consumable-operation")
                .bodyValue(consumableOperation1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        webTestClient.post().uri("/transactions/save-consumable-operation")
                .bodyValue(consumableOperation2)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        webTestClient.get().uri("/client/transactions")
                .attribute("clientId", userId)
                .attribute("transferType", TransferType.GOODS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetLimitAns.class)
                .consumeWith(result ->
                        assertEquals(new BigDecimal(-200), result.getResponseBody().getBody().get(0).getLimit().getLimit())
                );
    }
}