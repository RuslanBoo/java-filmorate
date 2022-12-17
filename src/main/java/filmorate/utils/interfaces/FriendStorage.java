package filmorate.utils.interfaces;

import filmorate.model.Friend;

public interface FriendStorage {
    Friend findFriend(Long userId, Long friendId);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    void applyFriend(Long userId, Long friendId);
}
