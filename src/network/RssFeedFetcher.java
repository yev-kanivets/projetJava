package network;

import entity.Article;
import entity.RssFeed;
import entity.Statistic;
import org.w3c.dom.Document;
import storage.ArticleStorage;
import storage.base.IStorage;
import util.Out;
import util.UniqueArticleFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Util class to encapsulate logic of periodical RSS feed fetching.
 * Created on 6/20/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedFetcher {

    private static final int CHECK_PERIOD = 1000;

    private final IStorage<RssFeed> rssFeedStorage;
    private final IStorage<Statistic> statisticStorage;

    private Thread fetcherTread;
    private boolean run;

    public RssFeedFetcher(IStorage<RssFeed> rssFeedStorage, IStorage<Statistic> statisticStorage) {
        this.rssFeedStorage = rssFeedStorage;
        this.statisticStorage = statisticStorage;
    }

    public void start() {
        if (run) {
            return;
        }
        run = true;

        fetcherTread = new FetcherThread();
        fetcherTread.setDaemon(true);
        fetcherTread.start();
    }

    public void stop() {
        if (!run) {
            return;
        }
        run = false;

        fetcherTread = null;
    }

    private class FetcherThread extends Thread {

        @Override
        public void run() {
            while (run) {
                Map<String, Statistic> statisticMap = new TreeMap<>();
                for (Statistic statistic : statisticStorage.getAll()) {
                    statisticMap.put(statistic.getRss(), statistic);
                }

                long currentTimestamp = System.currentTimeMillis();
                for (RssFeed rssFeed : rssFeedStorage.getAll()) {
                    if (currentTimestamp - rssFeed.getLastFetchTimestamp() >= rssFeed.getPeriod() * 1000) {
                        XmlHttpRequest xmlHttpRequest = new XmlHttpRequest(rssFeed.getUrl());
                        Document document = xmlHttpRequest.execute();

                        Statistic statistic = statisticMap.get(rssFeed.getName());
                        if (statistic == null) {
                            statistic = new Statistic(rssFeed.getName(), 0, 0, 0, 0);
                            statisticStorage.add(statistic);
                        }

                        int updateRequestCount = statistic.getUpdateRequestCount() + 1;
                        int failedUpdateRequestCount = statistic.getFailedUpdateRequestCount();
                        int articlesFetchedCount = statistic.getArticlesFetchedCount();
                        int articlesSavedCount = statistic.getArticlesSavedCount();

                        if (document == null) {
                            failedUpdateRequestCount++;
                            Out.get().error("Failed to fetch " + rssFeed);
                        } else {
                            RssFeedXmlParser parser = new RssFeedXmlParser(rssFeed, document);
                            List<Article> articleList = parser.parse();

                            articlesFetchedCount += articleList.size();
                            Out.get().info("Fetched " + articleList.size() + " articles from " + rssFeed.getName());

                            ArticleStorage articleStorage = new ArticleStorage(rssFeed);
                            UniqueArticleFilter filter = new UniqueArticleFilter(articleStorage.getLastSavedArticle());
                            articleList = filter.filter(new ArrayList<>(articleList));

                            articleStorage.addAll(articleList);

                            articlesSavedCount += articleList.size();
                            Out.get().info(articleList.size() + " new articles are added to storage.");
                        }

                        RssFeed newRssFeed = new RssFeed(rssFeed.getName(), rssFeed.getUrl(), rssFeed.getPeriod(),
                                currentTimestamp);
                        rssFeedStorage.update(newRssFeed);

                        Statistic newStatistic = new Statistic(rssFeed.getName(), updateRequestCount,
                                failedUpdateRequestCount, articlesFetchedCount, articlesSavedCount);
                        statisticStorage.update(newStatistic);
                    }
                }

                try {
                    Thread.sleep(CHECK_PERIOD);
                } catch (InterruptedException e) {
                    Out.get().trace(e);
                }
            }
        }
    }

}
