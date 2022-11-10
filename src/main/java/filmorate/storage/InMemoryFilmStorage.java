package filmorate.storage;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.model.Film;
import filmorate.storage.interfaces.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static Long nextID = 1L;
    private final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    @Override
    public Film create(Film film) {
        film.setId(nextID++);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильм для обновления с id %d не найден", film.getId()));
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм для обновления с id %d не найден", id));
        }
        return films.get(id);
    }
}
