package services;

import dto.TransactionDTO;
import org.springframework.stereotype.Service;
import repositories.TransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final TransactionRepository transactionRepository;
    public List<TransactionDTO> getLimitTransactions(UUID clientId) {

    }
}
