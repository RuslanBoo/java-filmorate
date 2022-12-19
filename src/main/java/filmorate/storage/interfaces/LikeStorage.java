package filmorate.storage.interfaces;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface LikeStorage {
    SqlRowSet getById(Long userLikedId, Long filmId);

    void create(Long userLikedId, Long filmId);

    void remove(Long userLikedId, Long filmId);
}
