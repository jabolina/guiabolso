package br.com.jabolina.guiabolso.service.impl;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.data.repository.TransactionRepository;
import br.com.jabolina.guiabolso.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> findTransactionByUserIdAndPeriod(User user, LocalDate start, LocalDate end) {
        log.info("Search transaction for user {} between {} and {}", user, start, end);
        return transactionRepository.findTransactionsForUserOnPeriod(user, start, end);
    }
}
