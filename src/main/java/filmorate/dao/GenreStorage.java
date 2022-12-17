package filmorate.dao;

import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.utils.interfaces.GenresStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreStorage implements GenresStorage {
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
    public Genre findById(Long id) throws EmptyResultDataAccessException {
        String sqlQuery = "SELECT genre_id, name FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    public void deleteFilmGenres(Film film) {
        String sqlDeleteQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteQuery, film.getId());
    }

    public void setFilmGenres(Long filmId, List<Genre> genres) {
        String sqlInsertGenreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        this.jdbcTemplate.batchUpdate(
                sqlInsertGenreQuery,
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, filmId);
                        preparedStatement.setLong(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }

                });
    }
}
