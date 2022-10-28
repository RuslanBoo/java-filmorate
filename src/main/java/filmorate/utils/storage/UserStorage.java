package filmorate.utils.storage;

import filmorate.utils.exceptions.UserNotFoundException;
import filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class UserStorage implements StorageManager<User>{
    private static int nextID = 1;
    private final Map<Integer, User> users;

    public UserStorage() {
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
        if(!users.containsKey(user.getId())){
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
}
