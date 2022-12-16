package filmorate.dao;

import filmorate.exceptions.filmExceptions.GenreNotFoundException;
import filmorate.model.Genre;
import filmorate.storage.interfaces.GenresStorage;
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
public class GenreDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getLong("genre_id"),
                resultSet.getString("name")
        );
    }

    @Override
    public Genre create(Genre genre) {
        log.error("Метод create не поддерживается в текущей реализации");
        throw new RuntimeException("Метод не поддерживается в текущей реализации");
    }

    @Override
    public Genre update(Genre genre) {
        log.error("Метод update не поддерживается в текущей реализации");
        throw new RuntimeException("Метод не поддерживается в текущей реализации");
    }

    @Override
    public Collection<Genre> getAll() {
        String sqlQuery = "SELECT genre_id, name FROM genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre findById(Long id) {
        try {
            String sqlQuery = "SELECT genre_id, name FROM genres WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new GenreNotFoundException("Жанр не найден");
        }
    }
}
