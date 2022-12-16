package filmorate.service;

import filmorate.dao.UserDbStorage;
import filmorate.exceptions.friendshipException.FriendshipAlreadyExistException;
import filmorate.exceptions.friendshipException.FriendshipNotFoundException;
import filmorate.model.Friendship;
import filmorate.model.User;
import filmorate.service.interfaces.FriendshipManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserDbService implements FriendshipManager {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            String sqlSelectQuery = "SELECT user_from, user_to " +
                    "FROM user_friend " +
                    "WHERE ( user_from = ? AND user_to = ? )" +
                    "OR ( user_to = ? AND user_from = ? )";

            Friendship friendship = jdbcTemplate.queryForObject(sqlSelectQuery,
                    userDbStorage::mapRowToFriendship,
                    userId,
                    friendId,
                    userId,
                    friendId
            );

            if (friendship.getUserTo() == userId && !friendship.isApplied()) {
                String sqlInsertQuery = "UPDATE user_friend SET is_applied = true " +
                        "WHERE user_from = ? AND user_to = ?";

                jdbcTemplate.update(sqlInsertQuery,
                        userId,
                        friendId
                );
            } else {
                log.error(String.format("Пользоватеть с id: %d уже отправил заявку в друзья пользователю с id: %d", userId, friendId));
                throw new FriendshipAlreadyExistException("Заявка в друзья уже отправлена");
            }

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            String sqlInsertQuery = "INSERT INTO user_friend (user_from, user_to) VALUES (?, ?)";

            jdbcTemplate.update(sqlInsertQuery,
                    userId,
                    friendId
            );
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        try {
            String sqlSelectQuery = "SELECT user_from, user_to " +
                    "FROM user_friend " +
                    "WHERE ( user_from = ? AND user_to = ? )" +
                    "OR ( user_to = ? AND user_from = ? )";

            Friendship friendship = jdbcTemplate.queryForObject(sqlSelectQuery,
                    userDbStorage::mapRowToFriendship,
                    userId,
                    friendId,
                    userId,
                    friendId
            );

            if (friendship.getUserFrom() == userId) {
                String sqlDeleteQuery = "DELETE FROM user_friend " +
                        "WHERE user_from = ? AND user_to = ?";
                jdbcTemplate.update(sqlDeleteQuery,
                        userId,
                        friendId
                );

                if (friendship.isApplied()) {
                    String sqlInsertNewFriendshipQuery = "INSERT INTO user_friend (user_from, user_to) VALUES (?, ?)";
                    jdbcTemplate.update(sqlInsertNewFriendshipQuery,
                            friendId,
                            userId
                    );
                }
            } else if (friendship.getUserTo() == userId && friendship.isApplied()) {
                String sqlUpdateQuery = "UPDATE user_friend SET is_applied = false " +
                        "WHERE user_from = ? AND user_to = ?";

                jdbcTemplate.update(sqlUpdateQuery,
                        friendId,
                        userId
                );
            }
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            log.error(String.format("Пользоватеть с id: %d не отправлял заявку в друзья пользователю с id: %d", userId, friendId));
            throw new FriendshipNotFoundException("Заявка в друзья  не найдена");
        }
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM users as u " +
                "JOIN user_friend AS uf " +
                "ON (u.user_id = uf.user_to AND uf.user_from = ?) " +
                "OR (u.user_id = uf.user_from AND uf.is_applied = true AND uf.user_to = ?)";

        return jdbcTemplate.query(sqlQuery, userDbStorage::mapRowToUser, userId, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        return getFriends(userId)
                .stream()
                .filter(getFriends(otherUserId)::contains)
                .collect(toList());
    }
}
