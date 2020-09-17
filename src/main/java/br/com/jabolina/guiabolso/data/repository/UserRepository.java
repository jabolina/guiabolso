package br.com.jabolina.guiabolso.data.repository;

import br.com.jabolina.guiabolso.data.domain.User;
import br.com.jabolina.guiabolso.storage.impl.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private final UserStorage storage;

    @Autowired
    public UserRepository(UserStorage storage) {
        this.storage = storage;
    }

    public Optional<User> findUserById(Integer id) {
        return storage.get(id);
    }

    public User saveUser(User user) {
        storage.put(user.getId(), user);
        return user;
    }
}
