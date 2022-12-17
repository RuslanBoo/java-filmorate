package filmorate.dao;

import filmorate.model.Film;
import filmorate.model.Mpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaStorage implements filmorate.utils.interfaces.MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getLong("rating_id"), resultSet.getString("name"));
    }

    @Override
    public Mpa create(Mpa mpa) {
        log.error("Метод create не поддерживается в текущей реализации");
        throw new RuntimeException("Метод create не поддерживается в текущей реализации");
    }

    @Override
    public Mpa update(Mpa mpa) {
        log.error("Метод update не поддерживается в текущей реализации");
        throw new RuntimeException("Метод update не поддерживается в текущей реализации");
    }

    @Override
    public Collection<Mpa> getAll() {
        String sqlQuery = "SELECT rating_id, name FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa findById(Long id) throws EmptyResultDataAccessException {
        String sqlQuery = "SELECT rating_id, name FROM mpa WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    public void deleteFilmMpa(Long filmId) {
        String sqlDeleteQuery = "DELETE FROM film_rating WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteQuery, filmId);
    }

    public void setFilmMpa(Film film) {
        String sqlInsertGenreQuery = "INSERT INTO film_rating (film_id, rating_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsertGenreQuery, film.getId(), film.getMpa().getId());
    }
}
