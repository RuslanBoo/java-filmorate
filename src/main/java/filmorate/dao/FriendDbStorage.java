package filmorate.dao;

import filmorate.model.Friend;
import filmorate.storage.interfaces.FriendStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public Friend mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return Friend.builder()
                .userFrom(resultSet.getLong("user_from"))
                .userTo(resultSet.getLong("user_to"))
                .build();
    }

    public void addFriend(Long userId, Long friendId) {
        String sqlInsertQuery = "INSERT INTO user_friend (user_from, user_to) VALUES (?, ?)";

        jdbcTemplate.update(sqlInsertQuery,
                userId,
                friendId
        );
    }

    public void removeFriend(Long userId, Long friendId) {
        String sqlDeleteQuery = "DELETE FROM user_friend WHERE user_from = ? AND user_to = ?";

        jdbcTemplate.update(sqlDeleteQuery,
                userId,
                friendId
        );
    }

    public Friend findFriend(Long userId, Long friendId) {
        String sqlSelectQuery = "SELECT user_from, user_to " +
                "FROM user_friend " +
                "WHERE ( user_from = ? AND user_to = ? )" +
                "OR ( user_to = ? AND user_from = ? )";

        return jdbcTemplate.queryForObject(sqlSelectQuery,
                this::mapRowToFriend,
                userId,
                friendId,
                userId,
                friendId
        );
    }

    public void applyFriend(Long userId, Long friendId) {
        String sqlInsertQuery = "UPDATE user_friend SET is_applied = true WHERE user_from = ? AND user_to = ?";

        jdbcTemplate.update(sqlInsertQuery,
                userId,
                friendId
        );
    }
}
