package controllers;

import dto.limit.MonthLimitRequest;
import dto.limit.MonthLimitResponse;
import dto.TransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import services.ClientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    @GetMapping("{clientId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getLimitTransactions(@PathVariable UUID clientId) {
        return new ResponseEntity<>(clientService.getLimitTransactions(clientId), HttpStatus.OK);
    }

    @PostMapping("/new-limit")
    public ResponseEntity<UUID> setNewLimit(@Valid @RequestBody MonthLimitRequest limitRequest) {
        return new ResponseEntity<>(clientService.setNewLimit(limitRequest), HttpStatus.CREATED);
    }
    @GetMapping("{clientId}/limits")
    public ResponseEntity<List<MonthLimitResponse>> getAllLimits(@PathVariable UUID clientId) {
        return new ResponseEntity<>(clientService.getAllLimits(clientId), HttpStatus.OK);
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
