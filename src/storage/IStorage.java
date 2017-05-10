package storage;

import java.util.List;

/**
 * Interface which describes API for storing, retrieving of entities.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public interface IStorage<T> {
    /**
     * @param t instance of T to be added to Storage
     * @return true if instance was added to Storage, false otherwise
     */
    boolean add(T t);

    /**
     * @return all instances that are stored in Storage at this moment
     */
    List<T> getAll();

    /**
     * @param t instance of T to be removed from Storage
     * @return true if instance was removed from Storage, false otherwise
     */
    boolean remove(T t);
}
