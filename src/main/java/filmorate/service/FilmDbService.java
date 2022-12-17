package filmorate.service;

import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.service.interfaces.FilmService;
import filmorate.service.interfaces.GenreService;
import filmorate.service.interfaces.LikeService;
import filmorate.service.interfaces.MpaService;
import filmorate.utils.enums.FilmSort;
import filmorate.utils.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.utils.interfaces.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class FilmDbService implements FilmService {
    private final LikeService likeService;
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Override
    public Film createFilm(Film film) {
        Film createdFilm = filmStorage.create(film);

        updateFilmMpa(film);
        updateFilmGenres(film);

        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            filmStorage.update(film);

            updateFilmMpa(film);
            updateFilmGenres(film);

            return filmStorage.findById(film.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    @Override
    public Film findFilmById(Long id) {
        try {
            return filmStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public void updateFilmGenres(Film film) {
        setUniqueGenres(film);
        genreService.updateFilmGenres(film);
    }

    @Override
    public void updateFilmMpa(Film film) {
        mpaService.updateFilmMpa(film);
    }

    @Override
    public void addLike(Long userLikedId, Long filmId) {
        likeService.addLike(userLikedId, filmId);
    }

    @Override
    public void removeLike(Long userLikedId, Long filmId) {
        likeService.removeLike(userLikedId, filmId);
    }

    @Override
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
