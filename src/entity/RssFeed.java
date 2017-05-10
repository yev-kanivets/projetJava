package entity;

/**
 * Entity class for RssFeed.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeed extends BaseEntity {

    public static final int DEFAULT_PERIOD = 15 * 60; // 15 minutes

    private String name;
    private String url;
    private int period; // in seconds
    private long lastUpdateTimestamp;

    public RssFeed(String name, String url, int period) {
        this.name = name;
        this.url = url;
        this.period = period;
        this.lastUpdateTimestamp = -1;
    }

    public RssFeed(String name, String url, int period, long lastUpdateTimestamp) {
        this.name = name;
        this.url = url;
        this.period = period;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
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
                    && lastUpdateTimestamp == rssFeed.getLastUpdateTimestamp();
        } else {
            return false;
        }
    }
}
