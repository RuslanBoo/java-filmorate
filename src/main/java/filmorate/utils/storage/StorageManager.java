package filmorate.utils.storage;

import java.util.Collection;

public interface StorageManager<T> {
    public T create(T t);

    public T update(T t);

    public Collection<T> getAll();
}
