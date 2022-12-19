package filmorate.storage.interfaces;

import java.util.Collection;

public interface StorageManager<T> {
    T create(T t);

    T update(T t);

    Collection<T> getAll();

    T getById(Long id);
}
