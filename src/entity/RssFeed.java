package entity;

/**
 * Entity class for RssFeed.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeed extends BaseEntity {

    public static final int DEFAULT_PERIOD = 15 * 60; // 15 minutes

    private final String name;
    private final String url;
    private final int period; // in seconds
    private final long lastFetchTimestamp;

    public RssFeed(String name, String url, int period) {
        this.name = name;
        this.url = url;
        this.period = period;
        this.lastFetchTimestamp = -1;
    }

    public RssFeed(String name, String url, int period, long lastFetchTimestamp) {
        this.name = name;
        this.url = url;
        this.period = period;
        this.lastFetchTimestamp = lastFetchTimestamp;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getPeriod() {
        return period;
    }

    public long getLastFetchTimestamp() {
        return lastFetchTimestamp;
    }

    @Override
    public String toString() {
        return name + " " + url + " " + period;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RssFeed) {
            RssFeed rssFeed = (RssFeed) obj;
            return isEquals(name, rssFeed.getName())
                    && isEquals(url, rssFeed.getUrl())
                    && period == rssFeed.getPeriod()
                    && lastFetchTimestamp == rssFeed.getLastFetchTimestamp();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + period;
        result = 31 * result + (int) (lastFetchTimestamp ^ (lastFetchTimestamp >>> 32));
        return result;
    }
}
