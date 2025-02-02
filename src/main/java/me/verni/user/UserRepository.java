package me.verni.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User createUser(String name, double initialBalance);

    User updateBalance(Long id, double newBalance);

    void deleteUser(Long id);

    void updateUser(User user);

    void close();
}
