package filmorate.service.interfaces;

import filmorate.model.Film;
import filmorate.utils.enums.FilmSort;

import java.util.Collection;

public interface LikesManager {
    void addLike(Long userLikedId, Long filmId);

    void removeLike(Long userLikedId, Long filmId);

    Collection<Film> getPopular(Integer size, FilmSort sort);
}
