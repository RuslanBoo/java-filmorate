package filmorate.controller;

import filmorate.utils.exceptions.UserNotFoundException;
import filmorate.model.User;
import filmorate.utils.storage.StorageManager;
import filmorate.utils.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final StorageManager<User> userStorage = new UserStorage();

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userStorage.create(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws UserNotFoundException{
        userStorage.update(user);
        return user;
    }
}