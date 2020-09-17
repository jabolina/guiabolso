package br.com.jabolina.guiabolso;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.mock.TransactionProperties;
import br.com.jabolina.guiabolso.service.impl.TransactionMockServiceImpl;
import br.com.jabolina.guiabolso.util.helpers.Generator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataGeneratorTest extends AbstractUT {
    private final Generator generator = new Generator();
    private final TransactionMockServiceImpl mockService = new TransactionMockServiceImpl();

    @Test
    public void shouldGenerateTransactionCorrectly() throws InstantiationException, IllegalAccessException {
        Transaction transaction = generator.generate(Transaction.class);

        Assert.assertNotNull("Generated transaction is null", transaction);
        Assert.assertTrue("Description is invalid", transaction.getDescription().length() >= 10 && transaction.getDescription().length() <= 60);
        Assert.assertTrue("Value is invalid", transaction.getValue() >= -9_999_999 && transaction.getValue() <= 9_999_999);
        Assert.assertNotNull("Date is null", transaction.getDate());
        log.info("Generated {}", transaction);
    }

    @Test
    public void shouldGenerateCompleteHistory() {
        LocalDate start = LocalDate.of(2020, 9, 1);
        TransactionProperties properties = TransactionProperties.builder()
                .withCopies(3)
                .withMonths(2)
                .withStart(start)
                .build();
        List<Transaction> transactions = mockService.mock(properties);

        Assert.assertNotNull("Transaction list is null", transactions);
        Assert.assertFalse("Transaction list is empty", transactions.isEmpty());
        log.info("Generated list {}", transactions);
    }

    @Test
    public void shouldHaveThreeMonthsWithDuplicates() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        TransactionProperties properties = TransactionProperties.builder()
                .withCopies(5)
                .withMonths(12)
                .withStart(start)
                .build();

        // This will generate transactions for 12 months.
        List<Transaction> transactions = mockService.mock(properties);

        // Now we will count how many months do have duplicates.
        int duplicateCounter = 0;
        Map<Integer, Boolean> monthsWithDuplicates = new HashMap<>();
        for (Transaction transaction: transactions) {
            int month = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().getValue();
            if (monthsWithDuplicates.containsKey(month)) {
                continue;
            }

            if (Boolean.TRUE.equals(transaction.getDuplicated())) {
                monthsWithDuplicates.put(month, Boolean.TRUE);
                duplicateCounter += 1;
            }
        }

        Assert.assertTrue("Have less than 3 duplicates!", duplicateCounter > 3);
        Assert.assertTrue("Have more than 12 duplicates!", duplicateCounter <= 12);
    }
}
