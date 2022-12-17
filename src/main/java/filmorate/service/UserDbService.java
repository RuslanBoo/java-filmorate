package filmorate.service;

import filmorate.model.Friend;
import filmorate.model.User;
import filmorate.service.interfaces.FriendManager;
import filmorate.service.interfaces.UserService;
import filmorate.utils.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.utils.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.utils.exceptions.userExceptions.UserNotFoundException;
import filmorate.utils.interfaces.FriendStorage;
import filmorate.utils.interfaces.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserDbService implements UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Override
    public void removeFriend(Long userId, Long friendId) {
        friendStorage.removeFriend(userId, friendId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        try {
            findById(user.getId());
            userStorage.update(user);
            return findById(user.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User findById(Long id) {
        try {
            return userStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        return userStorage.getFriends(userId)
                .stream()
                .filter(userStorage.getFriends(otherUserId)::contains)
                .collect(toList());
    }

    @Override
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
