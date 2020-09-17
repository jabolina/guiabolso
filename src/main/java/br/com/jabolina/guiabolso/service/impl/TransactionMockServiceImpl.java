package br.com.jabolina.guiabolso.service.impl;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.exception.GuiaBolsoError;
import br.com.jabolina.guiabolso.exception.GuiaBolsoException;
import br.com.jabolina.guiabolso.mock.MockDataProperties;
import br.com.jabolina.guiabolso.mock.TransactionProperties;
import br.com.jabolina.guiabolso.service.MockService;
import br.com.jabolina.guiabolso.util.helpers.AssertHelper;
import br.com.jabolina.guiabolso.util.helpers.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class TransactionMockServiceImpl implements MockService<List<Transaction>> {
    private final Generator generator;
    private final Random random;

    public TransactionMockServiceImpl() {
        this.generator = new Generator();
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public List<Transaction> mock(MockDataProperties mockProperties) {
        TransactionProperties properties = AssertHelper.isInstance(TransactionProperties.class,
                mockProperties, "Is not Transaction properties!");
        List<Transaction> transactions = new ArrayList<>();
        int period = properties.getMonths();
        LocalDate start = properties.getStart();
        LocalDate end = start.plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);

        try {
            while (period > 0) {
                transactions.addAll(generateForAMonth(properties, start, end));
                start = end;
                end = start.plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);
                period -= 1;
            }
        } catch (Exception e) {
            log.error("Failed generating transactions", e);
            throw new GuiaBolsoException(GuiaBolsoError.GENERATION_ERROR, "Failed generating transactions");
        }

        transactions.sort(Comparator.comparing(Transaction::getDate));
        return handleDuplicates(transactions);
    }

    /**
     * This method will randomly generate data for a month. Since the generated object contains
     * any date range, we bound the value to occur only inside a single month.
     * <p>
     * For each month we will create a random number of transactions, ranging from a minimum
     * of 10 transactions up to 30 transactions.
     * <p>
     * For each transaction generated, we have 25% chance of duplicating the value. The number
     * of duplicate will vary depending of the configuration value.
     * <p>
     * Using this approach we have a high probability that within a 12 month range that at least
     * 3 months will have a duplicate transaction. From the birthday paradox we can calculate this
     * probability.
     * <p>
     * Just like the birthday paradox, to find the chance for 3 months containing a duplicate transaction,
     * we must find the probability of not containing any duplicate. Since we use a uniform distribution
     * to generate how many transactions occur in a month, we have that in average each month will have
     * <p>
     * = 1/2 (a + b) = 1/2 (10 + 30) = 40/2 = 20 transactions per month
     * <p>
     * Lets calculate which is the probability for a duplicate transaction within a month. For a single
     * transaction it has 25% of chance of being manually duplicated, so the probability of not having
     * a duplicate in a month is:
     * <p>
     * T (1) = 0.75
     * T (2) = 0.75 * 0.75 = 0.5625
     * .
     * .
     * .
     * T (N) = 0.75 ^ N
     * <p>
     * Since, we have in average 20 transactions per month, this leads us that, a month without
     * duplicate transactions:
     * <p>
     * T (10) = 0.75 ^ 10 = 0.056 = 5.6% chance
     * <p>
     * Or that, 94.4% of the months do have at least 1 duplicate. Extending this approach, for 12
     * months that contains at most 2 months with duplicates we have the same as 10 months of
     * not having a single duplication. Which can be calculated as:
     * <p>
     * D (10) = 0.056 ^ 10 = 3.0330549e-13 = 0.00000000000030330549
     * <p>
     * So for having a whole year with at most 2 duplicates, we have 0.000000000030330549% chance.
     * The chance for wining the Mega da Virada with 6 numbers is 0.000002%.
     * <p>
     * Which means that virtually, for a 12 months period we will have 100% chance that more than
     * 2 duplicated transactions happens. This was a rough calculation with only the manually
     * duplicated entries.
     *
     * @param properties: the properties for generating the data.
     * @param start:      the lower date bound.
     * @param end:        the upper date bound.
     * @return a list of randomly generated transactions for a single month.
     * @throws InstantiationException: thrown if failed to generate a {@link Transaction}.
     * @throws IllegalAccessException: thrown if failed to generate a {@link Transaction}.
     */
    private List<Transaction> generateForAMonth(TransactionProperties properties, LocalDate start, LocalDate end)
            throws InstantiationException, IllegalAccessException {
        List<Transaction> transactions = new ArrayList<>();
        final int max = howMany(10, 30);

        for (int i = 0; i < max; i++) {
            Transaction transaction = generator.generate(Transaction.class);
            LocalDate date = generateDayBetween(start, end);
            LocalDateTime complete = date.atTime(generateTimeBetween());
            transaction.setDate(Date.from(complete.atZone(ZoneId.systemDefault()).toInstant()));
            boolean shouldDuplicate = Math.random() > 0.75;
            if (shouldDuplicate) {
                int howManyDuplicates = howMany(1, properties.getCopies());
                while (howManyDuplicates > 0) {
                    Transaction duplicate = new Transaction();
                    duplicate.setDuplicated(Boolean.TRUE);
                    duplicate.setDescription(transaction.getDescription());
                    duplicate.setDate(transaction.getDate());
                    duplicate.setValue(transaction.getValue());
                    transactions.add(duplicate);
                    howManyDuplicates -= 1;
                }
            }
            transactions.add(transaction);
        }

        return transactions;
    }

    /**
     * This method is responsible for marking the duplicated transactions
     * that were generated. Even though we already manually create duplicated
     * transaction, exists the chance the transaction also duplicate in a disjoint
     * generation.
     *
     * @param transactions: list with all transactions.
     * @return the list with transactions correctly marked.
     */
    private List<Transaction> handleDuplicates(List<Transaction> transactions) {
        for (int i = 0; i < transactions.size(); i += 1) {
            Transaction transaction = transactions.get(i);
            int indexOccurrence = transactions.indexOf(transaction);
            // indexOccurrence will never be -1
            transaction.setDuplicated(indexOccurrence != i);
        }
        return transactions;
    }

    /**
     * Generate a random time for a day. This time will range from
     * 00:00:00 up to 23:59:59.
     *
     * @return a random generated time.
     */
    private LocalTime generateTimeBetween() {
        int end = LocalTime.MAX.toSecondOfDay();
        int start = LocalTime.MIN.toSecondOfDay();
        int randomTime = ThreadLocalRandom.current()
                .nextInt(start, end);
        return LocalTime.ofSecondOfDay(randomTime);
    }

    /**
     * Generate a random date between the given range.
     *
     * @param start: the lower bound.
     * @param end:   the upper bound.
     * @return a random generated date between the needed range.
     */
    private LocalDate generateDayBetween(LocalDate start, LocalDate end) {
        long startEpoch = start.toEpochDay();
        long endEpoch = end.toEpochDay();
        long randomDay = ThreadLocalRandom.current()
                .nextLong(startEpoch, endEpoch);
        return LocalDate.ofEpochDay(randomDay);
    }

    /**
     * Create a random integer inside the given range. This will
     * generate a uniform distributed integer within the given range.
     * The generation is important to be uniform since we will use
     * this to generate N {@link Transaction} instances for a month.
     *
     * @param min: lower bound.
     * @param max: upper bound.
     * @return uniform random number.
     */
    private int howMany(int min, int max) {
        return random.nextInt(max) + min;
    }
}
