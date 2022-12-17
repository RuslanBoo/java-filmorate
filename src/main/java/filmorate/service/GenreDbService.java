package filmorate.service;

import filmorate.dao.GenreStorage;
import filmorate.model.Film;
import filmorate.model.Genre;
import filmorate.utils.exceptions.filmExceptions.GenreNotFoundException;
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
public class GenreDbService implements filmorate.service.interfaces.GenreService {
    private final GenreStorage genreStorage;

    @Override
    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre findById(Long id) {
        try {
            return genreStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new GenreNotFoundException("Жанр не найден");
        }
    }

    @Override
    public void updateFilmGenres(@Valid Film film) {
        genreStorage.deleteFilmGenres(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.setFilmGenres(film.getId(), film.getGenres());
        }
    }

}
