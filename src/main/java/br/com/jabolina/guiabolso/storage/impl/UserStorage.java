package br.com.jabolina.guiabolso.storage.impl;

import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.mock.UserProperties;
import br.com.jabolina.guiabolso.service.MockService;
import br.com.jabolina.guiabolso.service.impl.UserMockServiceImpl;
import br.com.jabolina.guiabolso.storage.Storage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class UserStorage implements Storage<Integer, User> {
    private final MockService<User> userMockService;
    private final Map<Integer, User> storage = Maps.newConcurrentMap();
    private final List<Integer> ignored = Lists.newArrayList();

    @Autowired
    public UserStorage(UserMockServiceImpl userMockService) {
        this.userMockService = userMockService;
    }

    @Override
    public User put(Integer integer, User user) {
        return storage.put(integer, user);
    }

    @Override
    public Optional<User> get(Integer id) {
        User user = storage.get(id);
        if (user == null && !ignored.contains(id)) {
            boolean shouldGenerateNewUser = Math.random() > 0.5;
            if (shouldGenerateNewUser) {
                log.info("Generating data on the fly for user {}", id);
                UserProperties properties = UserProperties.builder()
                        .withId(id)
                        .build();
                user = userMockService.mock(properties);
                put(id, user);
            } else {
                log.info("Will not generate data for user {}", id);
                ignored.add(id);
            }
        }
        return Optional.ofNullable(user);
    }
}
