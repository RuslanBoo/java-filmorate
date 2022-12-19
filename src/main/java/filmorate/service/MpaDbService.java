package filmorate.service;

import filmorate.exceptions.filmExceptions.MpaNotFoundException;
import filmorate.model.Film;
import filmorate.model.Mpa;
import filmorate.storage.interfaces.MpaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaDbService{
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    public Mpa getById(Long id) {
        try {
            return mpaStorage.getById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }

    public void updateFilmMpa(@Valid Film film) {
        mpaStorage.deleteFilmMpa(film.getId());
        if (film.getMpa() != null) {
            mpaStorage.setFilmMpa(film);
        }
    }
}
