package filmorate.dao;

import filmorate.exceptions.filmExceptions.MpaNotFoundException;
import filmorate.model.Mpa;
import filmorate.storage.interfaces.MpaStorage;
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
public class MpaDbStorage implements MpaStorage {
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
    public Mpa findById(Long id) {
        try {
            String sqlQuery = "SELECT rating_id, name FROM mpa WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }
}
