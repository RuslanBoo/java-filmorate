package filmorate.controller;

import filmorate.model.Genre;
import filmorate.storage.interfaces.StorageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final StorageManager<Genre> genreStorage;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreStorage.findById(id);
    }
}

