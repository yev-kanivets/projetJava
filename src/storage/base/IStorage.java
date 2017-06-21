package storage.base;

import java.util.Collection;
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
     * @param collection with elements to add
     * @return true if all of elements were added, false otherwise
     */
    boolean addAll(Collection<T> collection);

    /**
     * @return all instances that are stored in Storage at this moment
     */
    List<T> getAll();

    /**
     * @param t instance of T to be updated at Storage
     * @return true if instance was updated at Storage, false otherwise
     */
    boolean update(T t);

    /**
     * @param t instance of T to be removed from Storage
     * @return true if instance was removed from Storage, false otherwise
     */
    boolean remove(T t);
}
