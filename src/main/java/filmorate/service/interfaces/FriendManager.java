package filmorate.service.interfaces;

import filmorate.model.User;

import java.util.Collection;

public interface FriendManager {
    void addFriend(Long userId, Long FriendId);

    void removeFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long otherUserId);
}
