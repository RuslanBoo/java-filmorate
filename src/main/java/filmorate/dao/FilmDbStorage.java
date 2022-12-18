package filmorate.dao;

import filmorate.model.Film;
import filmorate.utils.enums.FilmSort;
import filmorate.utils.extractor.FilmExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmDbStorage{
    private final JdbcTemplate jdbcTemplate;

    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlInsertQuery = "INSERT INTO films (name, description, release_date, duration) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlInsertQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return film;
    }

    public Film update(Film film) throws EmptyResultDataAccessException {
        String sqlFilmUpdateQuery = "UPDATE films AS f " +
                "SET film_id = ?, name = ?, description = ?, release_date = ?, duration = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlFilmUpdateQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );

        return findById(film.getId());
    }

    public Collection<Film> getAll() {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, fr.rating_id, m.name AS rating_name, fg.genre_id, g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON m.rating_id = fr.rating_id " +
                "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id ";

        return jdbcTemplate.query(sqlQuery, new FilmExtractor());
    }

    public Film findById(Long id) throws EmptyResultDataAccessException {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, fr.rating_id, m.name AS rating_name, fg.genre_id, g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON m.rating_id = fr.rating_id " +
                "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "WHERE f.film_id = ?";

        List<Film> result = jdbcTemplate.query(sqlQuery, new FilmExtractor(), id);
        if (result == null || result.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return result.get(0);
        }
    }

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
