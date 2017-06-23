package entity;

/**
 * Entity class to keep track on statistic of RSS feed fetching.
 * Created on 6/22/17.
 *
 * @author Evgenii Kanivets
 */
public class Statistic extends BaseEntity {

    private final String rss;
    private final int updateRequestCount;
    private final int failedUpdateRequestCount;
    private final int articlesFetchedCount;
    private final int articlesSavedCount;

    public Statistic(String rss, int updateRequestCount, int failedUpdateRequestCount,
                     int articlesFetchedCount, int articlesSavedCount) {
        this.rss = rss;
        this.updateRequestCount = updateRequestCount;
        this.failedUpdateRequestCount = failedUpdateRequestCount;
        this.articlesFetchedCount = articlesFetchedCount;
        this.articlesSavedCount = articlesSavedCount;
    }

    public String getRss() {
        return rss;
    }

    public int getUpdateRequestCount() {
        return updateRequestCount;
    }

    public int getFailedUpdateRequestCount() {
        return failedUpdateRequestCount;
    }

    public int getArticlesFetchedCount() {
        return articlesFetchedCount;
    }

    public int getArticlesSavedCount() {
        return articlesSavedCount;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "rss='" + rss + '\'' +
                ", updateRequestCount=" + updateRequestCount +
                ", failedUpdateRequestCount=" + failedUpdateRequestCount +
                ", articlesFetchedCount=" + articlesFetchedCount +
                ", articlesSavedCount=" + articlesSavedCount +
                '}';
    }
}
