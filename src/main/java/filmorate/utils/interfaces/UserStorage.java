package filmorate.utils.interfaces;

import filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends StorageManager<User> {
    Collection<User> getFriends(Long userId);
}
