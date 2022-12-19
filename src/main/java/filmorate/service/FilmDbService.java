package filmorate.service;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.storage.interfaces.FilmStorage;
import filmorate.utils.enums.FilmSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbService implements FilmStorage {
    private final LikeDbService likeDbService;
    private final FilmStorage filmStorage;
    private final MpaDbService mpaDbService;
    private final GenreDbService genreDbService;

    public Film create(Film film) {
        Film createdFilm = filmStorage.create(film);

        updateFilmMpa(film);
        updateFilmGenres(film);

        return createdFilm;
    }

    public Film update(Film film) {
        try {
            filmStorage.update(film);

            updateFilmMpa(film);
            updateFilmGenres(film);

            return filmStorage.getById(film.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        try {
            return filmStorage.getById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    public void updateFilmGenres(Film film) {
        setUniqueGenres(film);
        genreDbService.updateFilmGenres(film);
    }

    public void updateFilmMpa(Film film) {
        mpaDbService.updateFilmMpa(film);
    }

    public void addLike(Long userLikedId, Long filmId) {
        likeDbService.addLike(userLikedId, filmId);
    }

    public void removeLike(Long userLikedId, Long filmId) {
        likeDbService.removeLike(userLikedId, filmId);
    }

    public Collection<Film> getPopular(Integer size, FilmSort sort) {
        return filmStorage.getPopular(size, sort);
    }

    private void setUniqueGenres(Film film) {
        if (film.getGenres() == null) {
            return;
        }

        List<Genre> genres = new ArrayList<>();

        for (Genre filmGenre : film.getGenres()) {
            if (!genres.contains(filmGenre)) {
                genres.add(filmGenre);
            }
        }

        film.setGenres(genres);
    }
}
