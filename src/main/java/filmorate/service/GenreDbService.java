package filmorate.service;

import filmorate.exceptions.filmExceptions.GenreNotFoundException;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.storage.interfaces.GenreStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreDbService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(Long id) {
        try {
            return genreStorage.getById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new GenreNotFoundException("Жанр не найден");
        }
    }

    public void updateFilmGenres(@Valid Film film) {
        genreStorage.deleteFilmGenres(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.setFilmGenres(film.getId(), film.getGenres());
        }
    }

}
