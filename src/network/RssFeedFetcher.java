package network;

import entity.Article;
import entity.RssFeed;
import org.w3c.dom.Document;
import storage.ArticleStorage;
import storage.base.IStorage;
import util.Out;
import util.UniqueArticleFilter;

import java.util.List;

/**
 * Util class to encapsulate logic of periodical RSS feed fetching.
 * Created on 6/20/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedFetcher {

    private static final int CHECK_PERIOD = 1000;

    private IStorage<RssFeed> rssFeedStorage;

    private Thread fetcherTread;
    private boolean run;

    public RssFeedFetcher(IStorage<RssFeed> rssFeedStorage) {
        this.rssFeedStorage = rssFeedStorage;
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
                long currentTimestamp = System.currentTimeMillis();
                for (RssFeed rssFeed : rssFeedStorage.getAll()) {
                    if (currentTimestamp - rssFeed.getLastFetchTimestamp() >= rssFeed.getPeriod() * 1000) {
                        XmlHttpRequest xmlHttpRequest = new XmlHttpRequest(rssFeed.getUrl());
                        Document document = xmlHttpRequest.execute();
                        if (document == null) {
                            Out.get().error("Failed to fetch " + rssFeed);
                        } else {
                            RssFeedXmlParser parser = new RssFeedXmlParser(rssFeed, document);
                            List<Article> articleList = parser.parse();

                            Out.get().info("Fetched " + articleList.size() + " articles from " + rssFeed.getName());

                            IStorage<Article> articleStorage = new ArticleStorage(rssFeed);
                            UniqueArticleFilter filter = new UniqueArticleFilter(articleStorage.getAll());
                            articleList = filter.filter(articleList);

                            articleStorage.addAll(articleList);
                            Out.get().info(articleList.size() + " new articles are added to storage.");
                        }

                        RssFeed newRssFeed = new RssFeed(rssFeed.getName(), rssFeed.getUrl(), rssFeed.getPeriod(),
                                currentTimestamp);
                        rssFeedStorage.update(newRssFeed);
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
