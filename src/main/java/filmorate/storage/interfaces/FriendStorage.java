package filmorate.storage.interfaces;

import filmorate.model.Friend;

public interface FriendStorage {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Friend findFriend(Long userId, Long friendId);

    void applyFriend(Long userId, Long friendId);
}
