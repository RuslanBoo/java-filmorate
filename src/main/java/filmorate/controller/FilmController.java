package filmorate.controller;

import filmorate.utils.exceptions.FilmNotFoundException;
import filmorate.model.Film;
import filmorate.utils.storage.FilmStorage;
import filmorate.utils.storage.StorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final StorageManager<Film> filmStorage = new FilmStorage();

    @GetMapping
    public Collection<Film> getAllFilms(){
        return filmStorage.getAll();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        filmStorage.create(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException{
        filmStorage.update(film);
        return film;
    }
}
