package filmorate.service;

import filmorate.dao.FriendDbStorage;
import filmorate.dao.UserDbStorage;
import filmorate.model.Friend;
import filmorate.model.User;
import filmorate.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
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
public class UserDbService{
    private final UserDbStorage userDbStorage;
    private final FriendDbStorage friendDbStorage;

    public void removeFriend(Long userId, Long friendId) {
        friendDbStorage.removeFriend(userId, friendId);
    }

    public User create(User user) {
        return userDbStorage.create(user);
    }

    public User update(User user) {
        try {
            findById(user.getId());
            userDbStorage.update(user);
            return findById(user.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    public User findById(Long id) {
        try {
            return userDbStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getFriends(Long userId) {
        return userDbStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        return userDbStorage.getFriends(userId)
                .stream()
                .filter(userDbStorage.getFriends(otherUserId)::contains)
                .collect(toList());
    }

    public void addFriend(Long userId, Long friendId) {
        try {
            Friend friendship = friendDbStorage.findFriend(userId, friendId);

            if (friendship == null) {
                throw new FriendshipNotFoundException("Заявка не найдена");
            } else if (friendship.getUserTo() == userId && !friendship.isApplied()) {
                friendDbStorage.applyFriend(userId, friendId);
            } else {
                log.error(String.format("Пользоватеть с id: %d уже отправил заявку в друзья пользователю с id: %d", userId, friendId));
                throw new FriendshipAlreadyExistException("Заявка в друзья уже отправлена");
            }
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            friendDbStorage.addFriend(userId, friendId);
        }
    }
}
