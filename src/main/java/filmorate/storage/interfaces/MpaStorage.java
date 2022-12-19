package filmorate.storage.interfaces;

import filmorate.model.Film;
import filmorate.model.Mpa;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collection;

public interface MpaStorage extends StorageManager<Mpa>{
    Mpa create(Mpa mpa);

    Mpa update(Mpa mpa);

    Collection<Mpa> getAll();

    Mpa getById(Long id) throws EmptyResultDataAccessException;

    void deleteFilmMpa(Long filmId);

    void setFilmMpa(Film film);
}
