package hamza.maharmeh.database.dao;

import java.util.List;

public interface Dao<T,S>{

    public void save(T t);
    public void delete(S primaryKey);
    public T get(S key);
    public List<T> getAll();
}
