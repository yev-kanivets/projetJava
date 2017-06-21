package storage.base;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class to encapsulate common storage methods.
 * Created on 6/21/17.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseStorage<T> implements IStorage<T> {

    protected List<T> storageList;

    protected abstract void saveToFile();

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public synchronized boolean add(T t) {
        if (t == null) {
            return false;
        }

        if (storageList.add(t)) {
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<T> collection) {
        if (collection == null) {
            return false;
        }

        if (storageList.addAll(collection)) {
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized List<T> getAll() {
        return new ArrayList<>(storageList);
    }

    @Override
    public boolean update(T t) {
        throw new UnsupportedMediaException();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public synchronized boolean remove(T t) {
        if (t == null) {
            return false;
        }

        if (storageList.remove(t)) {
            saveToFile();
            return true;
        } else {
            return false;
        }
    }
}
