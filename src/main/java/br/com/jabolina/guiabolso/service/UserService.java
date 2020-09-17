package br.com.jabolina.guiabolso.service;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;

import java.util.List;

public interface UserService {
    /**
     * Find all transactions owned by the given {@param userId}, where the transaction
     * was created in the given {@param month} and {@param year} up to the current month and year.
     *
     * @param userId: user that owns transactions.
     * @param month: starting month.
     * @param year: starting year.
     * @return a list of transactions.
     */
    List<Transaction> findTransactionsForPeriod(Integer userId, Integer month, Integer year);

    /**
     * Find a user by the given {@param id}.
     *
     * @param id: id to search.
     * @return a user that is identified by the id, or null if not found.
     */
    User getUserById(Integer id);
}
