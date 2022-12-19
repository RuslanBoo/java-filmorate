package filmorate.service;

import filmorate.storage.interfaces.FriendStorage;
import filmorate.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.model.Friend;
import filmorate.model.User;
import filmorate.storage.interfaces.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbService implements UserStorage {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public void removeFriend(Long userId, Long friendId) {
        friendStorage.removeFriend(userId, friendId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        try {
            getById(user.getId());
            userStorage.update(user);
            return getById(user.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(Long id) {
        try {
            return userStorage.getById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        return userStorage.getFriends(userId)
                .stream()
                .filter(userStorage.getFriends(otherUserId)::contains)
                .collect(toList());
    }

    public void addFriend(Long userId, Long friendId) {
        try {
            Friend friendship = friendStorage.findFriend(userId, friendId);

            if (friendship == null) {
                throw new FriendshipNotFoundException("Заявка не найдена");
            } else if (friendship.getUserTo() == userId && !friendship.isApplied()) {
                friendStorage.applyFriend(userId, friendId);
            } else {
                log.error(String.format("Пользоватеть с id: %d уже отправил заявку в друзья пользователю с id: %d", userId, friendId));
                throw new FriendshipAlreadyExistException("Заявка в друзья уже отправлена");
            }
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            friendStorage.addFriend(userId, friendId);
        }
    }
}
