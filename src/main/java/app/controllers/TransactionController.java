package app.controllers;

import app.dto.ConsumableOperation;
import app.dto.ratexchange.RateExchangeRequest;
import app.dto.ratexchange.RateExchangeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.services.TransactionService;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/save-consumable-operation")
    public ResponseEntity<UUID> saveConsumableOperation(@Valid @RequestBody ConsumableOperation consumableOperation) {
        return new ResponseEntity<>(transactionService.saveConsumableOperation(consumableOperation), HttpStatus.CREATED);
    }
    @GetMapping("/rate-exchange")
    public ResponseEntity<RateExchangeResponse> getStockExchangeData(@Valid @RequestBody RateExchangeRequest rateExchangeRequest) {
        return new ResponseEntity<>(transactionService.getStockExchangeData(rateExchangeRequest), HttpStatus.OK);
    }
}
