package filmorate.service;

import filmorate.dao.FilmDbStorage;
import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.model.Film;
import filmorate.model.Genre;
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
public class FilmDbService {
    private final LikeDbService likeDbService;
    private final FilmDbStorage filmDbStorage;
    private final MpaDbService mpaDbService;
    private final GenreDbService genreDbService;

    public Film createFilm(Film film) {
        Film createdFilm = filmDbStorage.create(film);

        updateFilmMpa(film);
        updateFilmGenres(film);

        return createdFilm;
    }

    public Film updateFilm(Film film) {
        try {
            filmDbStorage.update(film);

            updateFilmMpa(film);
            updateFilmGenres(film);

            return filmDbStorage.findById(film.getId());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAll();
    }

    public Film findFilmById(Long id) {
        try {
            return filmDbStorage.findById(id);
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
        return filmDbStorage.getPopular(size, sort);
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
