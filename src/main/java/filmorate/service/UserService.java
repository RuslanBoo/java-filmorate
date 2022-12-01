package filmorate.service;

import filmorate.exceptions.userExceptions.UserAlreadyInFriendsException;
import filmorate.exceptions.userExceptions.UserNotFoundException;
import filmorate.exceptions.userExceptions.UserNotFoundInFriendsException;
import filmorate.model.User;
import filmorate.service.interfaces.FriendshipManager;
import filmorate.storage.interfaces.UserStorage;
import filmorate.utils.enums.FriendListAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements FriendshipManager {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkUsersExist(userId, friendId);
        editFriendList(FriendListAction.ADD, userStorage.getById(userId), userStorage.getById(friendId));
        editFriendList(FriendListAction.ADD, userStorage.getById(friendId), userStorage.getById(userId));
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        checkUsersExist(userId, friendId);
        editFriendList(FriendListAction.REMOVE, userStorage.getById(userId), userStorage.getById(friendId));
        editFriendList(FriendListAction.REMOVE, userStorage.getById(friendId), userStorage.getById(userId));
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        checkUsersExist(userId);
        return userStorage.getById(userId)
                .getFriends()
                .stream()
                .map(userStorage::getById)
                .collect(toList());
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        checkUsersExist(userId);

        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherUserId);

        if(user.getFriends() == null || user.getFriends().isEmpty()){
            return getFriendList(otherUser);
        }

        if(otherUser.getFriends() == null || otherUser.getFriends().isEmpty()) {
            return getFriendList(user);
        }

        return user.getFriends()
                    .stream()
                    .filter(otherUser.getFriends()::contains)
                    .map(userStorage::getById)
                    .collect(toList());
    }

    private void editFriendList(FriendListAction action, User user, User friend) {
        Set<Long> newUserList = user.getFriends();

        switch (action) {
            case ADD:
                if (user.getFriends().contains(friend.getId())) {
                    throw new UserAlreadyInFriendsException(String.format("Пользователь с id: %d уже находится в списке друзей пользователя с id: %d", friend.getId(), user.getId()));
                }

                newUserList.add(friend.getId());
                break;

            case REMOVE:
                if (!user.getFriends().contains(friend.getId())) {
                    throw new UserNotFoundInFriendsException(String.format("Пользователь с id: %d не найден в списке друзей пользователя с id: %d", friend.getId(), user.getId()));
                }

                newUserList.remove(friend.getId());
                break;

            default:
        }

        user.setFriends(newUserList);
    }

    private Collection<User> getFriendList(User user) {
        return user.getFriends()
                .stream()
                .map(userStorage::getById)
                .collect(toList());
    }

    private void checkUsersExist(Long... usersIds){
        for(Long userId : usersIds){
            if(userStorage.getById(userId) == null){
                throw new UserNotFoundException(String.format("Пользователь с id: %d не найден", userId));
            }
        }
    }
}
