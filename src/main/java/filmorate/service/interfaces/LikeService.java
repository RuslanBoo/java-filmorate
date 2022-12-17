package filmorate.service.interfaces;

public interface LikeService {
    void addLike(Long userLikedId, Long filmId);

    void removeLike(Long userLikedId, Long filmId);
}
