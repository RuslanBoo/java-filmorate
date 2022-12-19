package filmorate.storage.interfaces;

import filmorate.model.Film;
import filmorate.model.Genre;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collection;
import java.util.List;

public interface GenreStorage extends StorageManager<Genre>{

    @Override
    Genre create(Genre genre);

    @Override
    Genre update(Genre genre);

    @Override
    Collection<Genre> getAll();

    Genre getById(Long id) throws EmptyResultDataAccessException;

    void deleteFilmGenres(Film film);

    void setFilmGenres(Long filmId, List<Genre> genres);
}

