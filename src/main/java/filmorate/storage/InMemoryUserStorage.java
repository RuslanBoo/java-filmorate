package filmorate.storage;

import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.model.User;
import filmorate.storage.interfaces.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static Long nextID = 1L;
    private final Map<Long, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User create(User user) {
        user.setId(nextID++);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);

        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("Пользователь для обновления с id %d не найден", user.getId()));
        }

        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);

        return user;
    }

    @Override
    public Collection<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());

        return users.values();
    }

    @Override
    public User getById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователь для обновления с id %d не найден", id));
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return null;
    }
}
