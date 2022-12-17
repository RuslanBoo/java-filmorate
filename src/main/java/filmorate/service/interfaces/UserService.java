package filmorate.service.interfaces;

import filmorate.model.User;

import java.util.Collection;

public interface UserService extends FriendManager{

    User create(User user);

    User update(User user);

    Collection<User> getAll();

    User findById(Long id);

    @Override
    void addFriend(Long userId, Long friendId);

    @Override
    void removeFriend(Long userId, Long friendId);

    @Override
    Collection<User> getFriends(Long userId);

    @Override
    Collection<User> getCommonFriends(Long userId, Long otherUserId);

}
