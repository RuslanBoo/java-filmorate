package filmorate.service;

import filmorate.dao.FilmDbStorage;
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
    private final FilmDbStorage filmStorage;

    @Override
    public void addLike(Long userLikedId, Long filmId) {
        String sqlQuery = "INSERT INTO film_like (FILM_ID, USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userLikedId
        );
    }

    @Override
    public void removeLike(Long userLikedId, Long filmId) {
        String sqlSelectQuery = "SELECT film_id, user_id FROM film_like WHERE FILM_ID = ? AND USER_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlSelectQuery, filmId, userLikedId);

        if (sqlRowSet.first()) {
            String sqlDeleteQuery = "DELETE FROM film_like WHERE FILM_ID = ? AND USER_ID = ?";

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

        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, fr.RATING_ID, m.NAME AS rating_name, fg.GENRE_ID, g.NAME AS genre_name, COUNT(fl.user_id) AS count " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.FILM_ID = f.FILM_ID " +
                "LEFT JOIN mpa AS m ON m.RATING_ID = fr.RATING_ID " +
                "LEFT JOIN film_genre AS fg ON fg.FILM_ID = f.FILM_ID " +
                "LEFT JOIN film_like AS fl ON fl.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.GENRE_ID = fg.GENRE_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, new FilmExtractor(), size);
    }
}
