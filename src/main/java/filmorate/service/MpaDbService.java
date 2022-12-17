package filmorate.service;

import filmorate.dao.MpaStorage;
import filmorate.model.Film;
import filmorate.model.Mpa;
import filmorate.utils.exceptions.filmExceptions.MpaNotFoundException;
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
public class MpaDbService implements filmorate.service.interfaces.MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public Collection<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa findById(Long id) {
        try {
            return mpaStorage.findById(id);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }

    @Override
    public void updateFilmMpa(@Valid Film film) {
        mpaStorage.deleteFilmMpa(film.getId());
        if (film.getMpa() != null) {
            mpaStorage.setFilmMpa(film);
        }
    }
}
