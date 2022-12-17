package filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeStorage implements filmorate.utils.interfaces.LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public SqlRowSet findLikeByIds(Long userLikedId, Long filmId) {
        String sqlSelectQuery = "SELECT film_id, user_id FROM film_like WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.queryForRowSet(sqlSelectQuery, filmId, userLikedId);
    }

    public void addLike(Long userLikedId, Long filmId) {
        String sqlQuery = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userLikedId
        );
    }

    public void removeLike(Long userLikedId, Long filmId) {
        String sqlDeleteQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlDeleteQuery,
                filmId,
                userLikedId
        );
    }
}
