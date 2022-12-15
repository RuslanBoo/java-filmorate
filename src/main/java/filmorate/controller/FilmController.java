package filmorate.controller;

import filmorate.exceptions.filmExceptions.FilmNotFoundException;
import filmorate.model.Film;
import filmorate.service.interfaces.LikesManager;
import filmorate.storage.interfaces.StorageManager;
import filmorate.utils.enums.FilmSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final StorageManager<Film> filmStorage;
    private final LikesManager filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmStorage.findById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count
    ) {
        return filmService.getPopular(count, FilmSort.DESC);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException {
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") Long filmId, @PathVariable(name = "userId") Long userId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") Long filmId, @PathVariable(name = "userId") Long userId) {
        filmService.removeLike(userId, filmId);
    }
}
