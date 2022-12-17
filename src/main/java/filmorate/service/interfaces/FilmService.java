package filmorate.service.interfaces;

import filmorate.model.Film;

import java.util.Collection;

public interface FilmService extends LikesManager {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film findFilmById(Long id);

    void updateFilmGenres(Film film);

    void updateFilmMpa(Film film);
}
