package filmorate.storage.interfaces;

import filmorate.model.Film;
import filmorate.utils.enums.FilmSort;

import java.util.Collection;

public interface FilmStorage extends StorageManager<Film> {
    Collection<Film> getPopular(Integer size, FilmSort sort);
}
