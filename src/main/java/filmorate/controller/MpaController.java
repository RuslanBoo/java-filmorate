package filmorate.controller;

import filmorate.model.Mpa;
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
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final StorageManager<Mpa> mpaStorage;

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable Long id) {
        return mpaStorage.findById(id);
    }
}

