package network;

import entity.RssFeed;
import org.w3c.dom.Document;
import storage.IStorage;

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
                            System.out.println("Failed to fetch " + rssFeed);
                        } else {
                            rssFeed.setLastFetchTimestamp(currentTimestamp);
                            System.out.println("Fetched " + rssFeed.getName());
                        }
                    }
                }

                try {
                    Thread.sleep(CHECK_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
