package filmorate.utils.storage;

import filmorate.utils.exceptions.FilmNotFoundException;
import filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmStorage implements StorageManager<Film> {
    private static int nextID = 1;
    private final Map<Integer, Film> films;

    public FilmStorage() {
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
}
