package br.com.jabolina.guiabolso.service;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    /**
     * Find all transactions where the user is the owner and was created between
     * the given period.
     *
     * @param user:  user that owns the transactions.
     * @param start: starting search date.
     * @param end:   ending search date.
     * @return list of transactions.
     */
    List<Transaction> findTransactionByUserIdAndPeriod(User user, LocalDate start, LocalDate end);
}
