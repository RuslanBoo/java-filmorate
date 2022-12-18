package filmorate.service;

import filmorate.dao.LikeDbStorage;
import filmorate.exceptions.likeException.LikeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbService{
    private final LikeDbStorage likeDbStorage;

    public boolean isLikeExist(Long userLikedId, Long filmId) {
        return likeDbStorage.findLikeByIds(userLikedId, filmId).first();
    }

    public void addLike(Long userLikedId, Long filmId) {
        likeDbStorage.addLike(userLikedId, filmId);
    }

    public void removeLike(Long userLikedId, Long filmId) {
        if (isLikeExist(userLikedId, filmId)) {
            likeDbStorage.removeLike(userLikedId, filmId);
        } else {
            throw new LikeNotFoundException("Лайк не найден");
        }
    }
}
