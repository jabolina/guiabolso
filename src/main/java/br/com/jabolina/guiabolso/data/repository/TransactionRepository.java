package br.com.jabolina.guiabolso.data.repository;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.mock.TransactionProperties;
import br.com.jabolina.guiabolso.service.MockService;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final UserRepository userRepository;
    private final MockService<List<Transaction>> mockService;

    public TransactionRepository(UserRepository userRepository, MockService<List<Transaction>> mockService) {
        this.userRepository = userRepository;
        this.mockService = mockService;
    }

    private boolean filterByDate(Transaction transaction, LocalDate start, LocalDate end) {
        LocalDateTime beginningOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.atTime(LocalTime.MAX);
        LocalDateTime occurrence = LocalDateTime.ofInstant(transaction.getDate().toInstant(), ZoneId.systemDefault());
        return occurrence.isAfter(beginningOfDay) && occurrence.isBefore(endOfDay);
    }

    /**
     * This will search and find all transaction between the given dates for the given user.
     *
     * If this was connected to a database, a query would be placed here, but since this is
     * only a mock, first will be verified if exist any information on the user in the given
     * period of time.
     *
     * If nothing exists, then new data will be generated and added to the already existent
     * transactions. If the any data already exists, then it will be used.
     *
     * @param user: owner of the transactions.
     * @param start: transaction start period.
     * @param end: transaction end period.
     * @return list of transactions that happened during the period.
     */
    public List<Transaction> findTransactionsForUserOnPeriod(User user, LocalDate start, LocalDate end) {
        List<Transaction> transactions = user.getTransactions().stream()
                .filter(transaction -> filterByDate(transaction, start, end))
                .collect(Collectors.toList());

        if (transactions.isEmpty()) {
            TransactionProperties properties = TransactionProperties.builder()
                    .withCopies(5)
                    .withMonths(3)
                    .withStart(start)
                    .build();
            List<Transaction> allTransactions = user.getTransactions();
            allTransactions.addAll(mockService.mock(properties));
            user.setTransactions(allTransactions);
            // This does not feel right.
            // If the data was persisted, the would be no need to save
            // anything, just to read, so turn a blind eye here please.
            userRepository.saveUser(user);

            return findTransactionsForUserOnPeriod(user, start, end);
        }
        return transactions;
    }

}
