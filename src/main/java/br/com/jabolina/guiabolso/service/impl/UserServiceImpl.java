package br.com.jabolina.guiabolso.service.impl;

import br.com.jabolina.guiabolso.data.domain.Transaction;
import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.data.repository.UserRepository;
import br.com.jabolina.guiabolso.service.TransactionService;
import br.com.jabolina.guiabolso.service.UserService;
import br.com.jabolina.guiabolso.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final ValidationService validationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TransactionService transactionService,
                           ValidationService validationService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.validationService = validationService;
    }

    @Override
    public List<Transaction> findTransactionsForPeriod(Integer userId, Integer month, Integer year) {
        validationService.validate(() -> {
            Assert.isTrue(month > 0 && month < 13, "Month value is not valid!");
            Assert.isTrue(year > 0, "Year value is not valid!");
            Assert.isTrue(userId >= 1_000 && userId <= 100_000_000, "User id is not valid!");
        });

        Optional<User> optionalUser = userRepository.findUserById(userId);
        if (!optionalUser.isPresent()) {
            return Collections.emptyList();
        }
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plus(1, ChronoUnit.MONTHS)
                .minus(1, ChronoUnit.DAYS);
        return transactionService.findTransactionByUserIdAndPeriod(optionalUser.get(), start, end);
    }

    @Override
    public User getUserById(Integer id) {
        validationService.validate(() -> Assert.isTrue(id > 1_000 && id < 100_000_000, "User id is not valid!"));
        log.info("Search for user with ID {}", id);
        return userRepository.findUserById(id).orElse(null);
    }
}
