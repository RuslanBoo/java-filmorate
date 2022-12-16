package filmorate.dao;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.extractor.FilmExtractor;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.storage.interfaces.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
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
        updateFilmMpa(film);
        updateFilmGenres(film);

        return film;
    }

    @Override
    public Film update(Film film) {
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

        updateFilmMpa(film);
        updateFilmGenres(film);

        return findById(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, fr.rating_id, m.name AS rating_name, fg.genre_id, g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON m.rating_id = fr.rating_id " +
                "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id ";

        return jdbcTemplate.query(sqlQuery, new FilmExtractor());
    }

    @Override
    public Film findById(Long id) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, fr.rating_id, m.name AS rating_name, fg.genre_id, g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id " +
                "LEFT JOIN mpa AS m ON m.rating_id = fr.rating_id " +
                "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id " +
                "WHERE f.film_id = ?";

        ArrayList<Film> result = jdbcTemplate.query(sqlQuery, new FilmExtractor(), id);
        if(result == null || result.isEmpty()){
            throw new FilmNotFoundException("Фильм не найден");
        } else {
            return result.get(0);
        }
    }

    public void updateFilmGenres(Film film) {
        String sqlDeleteQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteQuery, film.getId());


        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> uniqueGenres = new HashSet<>(film.getGenres());
            List<Genre> genres = new ArrayList<>(uniqueGenres);
            String sqlInsertGenreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

            this.jdbcTemplate.batchUpdate(
                    sqlInsertGenreQuery,
                    new BatchPreparedStatementSetter() {

                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setLong(1, film.getId());
                            preparedStatement.setLong(2, genres.get(i).getId());
                        }

                        public int getBatchSize() {
                            return genres.size();
                        }

                    });
        }
    }

    public void updateFilmMpa(Film film) {
        String sqlDeleteQuery = "DELETE FROM film_rating WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteQuery, film.getId());

        if (film.getMpa() != null) {
            String sqlInsertGenreQuery = "INSERT INTO film_rating (film_id, rating_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlInsertGenreQuery, film.getId(), film.getMpa().getId());
        }
    }
}
