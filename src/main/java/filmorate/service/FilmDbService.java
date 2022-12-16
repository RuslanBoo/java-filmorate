package filmorate.service;

import filmorate.exceptions.likeException.LikeNotFoundException;
import filmorate.extractor.FilmExtractor;
import filmorate.model.Film;
import filmorate.service.interfaces.LikesManager;
import filmorate.utils.enums.FilmSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class FilmDbService implements LikesManager {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long userLikedId, Long filmId) {
        String sqlQuery = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userLikedId
        );
    }

    @Override
    public void removeLike(Long userLikedId, Long filmId) {
        String sqlSelectQuery = "SELECT film_id, user_id FROM film_like WHERE film_id = ? AND user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlSelectQuery, filmId, userLikedId);

        if (sqlRowSet.first()) {
            String sqlDeleteQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";

            jdbcTemplate.update(sqlDeleteQuery,
                    filmId,
                    userLikedId
            );
        } else {
            throw new LikeNotFoundException("Лайк не найден");
        }
    }

    @Override
    public Collection<Film> getPopular(Integer size, FilmSort sort) {

        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, fr.rating_id, m.name AS rating_name, fg.genre_id, g.name AS genre_name, COUNT(fl.user_id) AS count " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON m.rating_id = fr.rating_id " +
                "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "LEFT JOIN film_like AS fl ON fl.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, new FilmExtractor(), size);
    }
}
