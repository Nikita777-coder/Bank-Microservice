package app.controllers;

import app.dto.limit.LimitTransactionsRequest;
import app.dto.limit.MonthLimitRequest;
import app.dto.TransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import app.services.ClientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getLimitTransactions(
            @Valid @RequestBody LimitTransactionsRequest limitTransactionsRequest) {
        return new ResponseEntity<>(clientService.getLimitTransactions(limitTransactionsRequest), HttpStatus.OK);
    }

    @PostMapping("/new-limit")
    public ResponseEntity<UUID> setNewLimit(@Valid @RequestBody MonthLimitRequest limitRequest) {
        return new ResponseEntity<>(clientService.setNewLimit(limitRequest), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
