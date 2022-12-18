package filmorate.exceptions.userExceptions;

public class UserNotFoundInFriendsException extends RuntimeException {
    public UserNotFoundInFriendsException(String message) {
        super(message);
    }
}
