package filmorate.service;

import filmorate.dao.GenreDbStorage;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.exceptions.filmExceptions.GenreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class GenreDbService {
    private final GenreDbStorage genreDbStorage;

    public Collection<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    public Genre findById(Long id) {
        try {
            return genreDbStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new GenreNotFoundException("Жанр не найден");
        }
    }

    public void updateFilmGenres(@Valid Film film) {
        genreDbStorage.deleteFilmGenres(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreDbStorage.setFilmGenres(film.getId(), film.getGenres());
        }
    }

}
