package filmorate.service;

import filmorate.model.Film;
import filmorate.model.User;
import filmorate.service.interfaces.LikesManager;
import filmorate.storage.interfaces.FilmStorage;
import filmorate.storage.interfaces.UserStorage;
import filmorate.utils.enums.FilmLikeAction;
import filmorate.utils.enums.FilmSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements LikesManager {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(Long userLikedId, Long filmId) {
        changeLike(FilmLikeAction.ADD, filmStorage.getById(filmId), userStorage.getById(userLikedId));
    }

    @Override
    public void removeLike(Long userLikedId, Long filmId) {
        changeLike(FilmLikeAction.REMOVE, filmStorage.getById(filmId), userStorage.getById(userLikedId));
    }

    @Override
    public Collection<Film> getPopular(Integer size, FilmSort sort) {
        return filmStorage
                .getAll()
                .stream()
                .sorted((f0, f1) -> {
                            int comp = Integer.compare(f1.getUsersLike().size(), f0.getUsersLike().size());
                            if (sort == FilmSort.DESC) {
                                comp *= -1;
                            }
                            return comp;
                        }
                )
                .limit(size)
                .collect(Collectors.toList());
    }

    private void changeLike(FilmLikeAction action, Film film, User user) {
        Set<Long> newLikesList = film.getUsersLike();

        switch (action) {
            case ADD:
                newLikesList.add(user.getId());
                break;

            case REMOVE:
                newLikesList.remove(user.getId());
                break;
            default:
        }

        film.setUsersLike(newLikesList);
    }
}
