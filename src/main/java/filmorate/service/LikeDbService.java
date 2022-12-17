package filmorate.service;

import filmorate.dao.LikeStorage;
import filmorate.service.interfaces.LikeService;
import filmorate.utils.exceptions.likeException.LikeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbService implements LikeService {
    private final LikeStorage likeStorage;

    public boolean isLikeExist(Long userLikedId, Long filmId) {
        return likeStorage.findLikeByIds(userLikedId, filmId).first();
    }

    @Override
    public void addLike(Long userLikedId, Long filmId) {
        likeStorage.addLike(userLikedId, filmId);
    }

    @Override
    public void removeLike(Long userLikedId, Long filmId) {
        if (isLikeExist(userLikedId, filmId)) {
            likeStorage.removeLike(userLikedId, filmId);
        } else {
            throw new LikeNotFoundException("Лайк не найден");
        }
    }
}
