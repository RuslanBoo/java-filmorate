package filmorate.controller;

import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.model.User;
import filmorate.service.interfaces.FriendshipManager;
import filmorate.storage.interfaces.StorageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final StorageManager<User> userStorage;
    private final FriendshipManager userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable(name = "id") Long userId) {
        return userStorage.getById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(
            @PathVariable(name = "id") Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable(name = "id") Long userId,
            @PathVariable(name = "otherId") Long otherUserId) {
        return userService.getCommonFriends(userId, otherUserId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws UserNotFoundException {
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(name = "id") Long userId, @PathVariable(name = "friendId") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable(name = "id") Long userId, @PathVariable(name = "friendId") Long friendId) {
        userService.removeFriend(userId, friendId);
    }
}
