package filmorate.service.interfaces;

import filmorate.model.Film;
import filmorate.model.Genre;

import java.util.Collection;

public interface GenreService {
    Collection<Genre> getAll();

    Genre findById(Long id);

    void updateFilmGenres(Film film);
}
