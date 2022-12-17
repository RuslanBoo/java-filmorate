package filmorate.utils.interfaces;

public interface LikeStorage {
    void addLike(Long userLikedId, Long filmId);

    void removeLike(Long userLikedId, Long filmId);
}
