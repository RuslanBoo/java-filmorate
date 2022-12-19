package filmorate.service;

import filmorate.exceptions.likeException.LikeNotFoundException;
import filmorate.storage.interfaces.LikeStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbService{
    private final LikeStorage likeStorage;

    public boolean isLikeExist(Long userLikedId, Long filmId) {
        return likeStorage.getById(userLikedId, filmId).first();
    }

    public void addLike(Long userLikedId, Long filmId) {
        likeStorage.create(userLikedId, filmId);
    }

    public void removeLike(Long userLikedId, Long filmId) {
        if (isLikeExist(userLikedId, filmId)) {
            likeStorage.remove(userLikedId, filmId);
        } else {
            throw new LikeNotFoundException("Лайк не найден");
        }
    }
}
