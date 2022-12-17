package filmorate.utils.extractor;

import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.model.Mpa;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    public List<Film> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Film> map = new HashMap<>();

        while (resultSet.next()) {
            Long id = resultSet.getLong("film_id");
            Film film = map.get(id);

            if (film == null) {
                film = Film.builder()
                        .id(resultSet.getLong("film_id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .releaseDate(LocalDate.parse(resultSet.getString("release_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .duration(resultSet.getInt("duration"))
                        .build();
                map.put(id, film);
            }

            List<Genre> genresList = film.getGenres();
            if (genresList == null) {
                genresList = new ArrayList<>();
            }

            if (resultSet.getLong("genre_id") > 0) {
                Genre genre = new Genre(
                        resultSet.getLong("genre_id"),
                        resultSet.getString("genre_name")
                );

                genresList.add(genre);
            }

            film.setGenres(genresList);

            if (resultSet.getLong("rating_id") > 0) {
                Mpa mpa = new Mpa(resultSet.getLong("rating_id"), resultSet.getString("rating_name"));
                film.setMpa(mpa);
            }
        }
        return new ArrayList<>(map.values());
    }
}