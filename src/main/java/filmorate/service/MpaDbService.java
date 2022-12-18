package filmorate.service;

import filmorate.dao.MpaDbStorage;
import filmorate.model.Film;
import filmorate.model.Mpa;
import filmorate.exceptions.filmExceptions.MpaNotFoundException;
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
public class MpaDbService{
    private final MpaDbStorage mpaDbStorage;

    public Collection<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }

    public Mpa findById(Long id) {
        try {
            return mpaDbStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }

    public void updateFilmMpa(@Valid Film film) {
        mpaDbStorage.deleteFilmMpa(film.getId());
        if (film.getMpa() != null) {
            mpaDbStorage.setFilmMpa(film);
        }
    }
}
