package filmorate.service.interfaces;

import filmorate.model.Film;
import filmorate.model.Mpa;

import java.util.Collection;

public interface MpaService {
    Collection<Mpa> getAll();

    Mpa findById(Long id);

    void updateFilmMpa(Film film);
}
