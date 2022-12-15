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
            String sqlSelectQuery = "SELECT USER_FROM, USER_TO " +
                    "FROM user_friend " +
                    "WHERE ( USER_FROM = ? AND USER_TO = ? )" +
                    "OR ( USER_TO = ? AND USER_FROM = ? )";

            Friendship friendship = jdbcTemplate.queryForObject(sqlSelectQuery,
                    userDbStorage::mapRowToFriendship,
                    userId,
                    friendId,
                    userId,
                    friendId
            );

            if (friendship.getUserTo() == userId && !friendship.isApplied()) {
                String sqlInsertQuery = "UPDATE user_friend SET IS_APPLIED = true " +
                        "WHERE USER_FROM = ? AND USER_TO = ?";

                jdbcTemplate.update(sqlInsertQuery,
                        userId,
                        friendId
                );
            } else {
                log.error(String.format("Пользоватеть с id: %d уже отправил заявку в друзья пользователю с id: %d", userId, friendId));
                throw new FriendshipAlreadyExistException("Заявка в друзья уже отправлена");
            }

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            String sqlInsertQuery = "INSERT INTO user_friend (USER_FROM, USER_TO) VALUES (?, ?)";

            jdbcTemplate.update(sqlInsertQuery,
                    userId,
                    friendId
            );
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        try {
            String sqlSelectQuery = "SELECT USER_FROM, USER_TO " +
                    "FROM user_friend " +
                    "WHERE ( USER_FROM = ? AND USER_TO = ? )" +
                    "OR ( USER_TO = ? AND USER_FROM = ? )";

            Friendship friendship = jdbcTemplate.queryForObject(sqlSelectQuery,
                    userDbStorage::mapRowToFriendship,
                    userId,
                    friendId,
                    userId,
                    friendId
            );

            if (friendship.getUserFrom() == userId) {
                String sqlDeleteQuery = "DELETE FROM user_friend " +
                        "WHERE USER_FROM = ? AND USER_TO = ?";
                jdbcTemplate.update(sqlDeleteQuery,
                        userId,
                        friendId
                );

                if (friendship.isApplied()) {
                    String sqlInsertNewFriendshipQuery = "INSERT INTO user_friend (USER_FROM, USER_TO) VALUES (?, ?)";
                    jdbcTemplate.update(sqlInsertNewFriendshipQuery,
                            friendId,
                            userId
                    );
                }
            } else if (friendship.getUserTo() == userId && friendship.isApplied()) {
                String sqlUpdateQuery = "UPDATE user_friend SET IS_APPLIED = false " +
                        "WHERE USER_FROM = ? AND USER_TO = ?";

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
        String sqlQuery = "SELECT u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "FROM USERS as u " +
                "JOIN user_friend AS uf " +
                "ON (u.user_id = uf.USER_TO AND uf.USER_FROM = ?) " +
                "OR (u.user_id = uf.USER_FROM AND uf.IS_APPLIED = true AND uf.USER_TO = ?)";

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
