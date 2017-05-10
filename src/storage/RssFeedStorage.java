package storage;

import entity.RssFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link IStorage} interface to work with {@link RssFeed} entities.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedStorage implements IStorage<RssFeed> {

    private List<RssFeed> rssFeedList;

    public RssFeedStorage() {
        rssFeedList = new ArrayList<>();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean add(RssFeed rssFeed) {
        if (rssFeed == null) {
            return false;
        }
        return rssFeedList.add(rssFeed);
    }

    @Override
    public List<RssFeed> getAll() {
        return rssFeedList;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean remove(RssFeed rssFeed) {
        if (rssFeed == null) {
            return false;
        }
        return rssFeedList.remove(rssFeed);
    }

}
