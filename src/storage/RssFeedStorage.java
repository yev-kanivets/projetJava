package storage;

import entity.RssFeed;
import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implementation of {@link IStorage} interface to work with {@link RssFeed} entities.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedStorage implements IStorage<RssFeed> {

    private static final String FILENAME = "default.cfg";

    private List<RssFeed> rssFeedList;

    public RssFeedStorage() {
        rssFeedList = new ArrayList<>();
        readFromFile();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public synchronized boolean add(RssFeed rssFeed) {
        if (rssFeed == null) {
            return false;
        }

        if (rssFeedList.add(rssFeed)) {
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized List<RssFeed> getAll() {
        return new ArrayList<>(rssFeedList);
    }

    @Override
    public synchronized boolean update(RssFeed rssFeed) {
        if (rssFeed == null) {
            return false;
        }

        RssFeed rssFeedToUpdate = null;
        for (RssFeed item : rssFeedList) {
            if (rssFeed.getName().equals(item.getName())) {
                rssFeedToUpdate = item;
            }
        }

        if (rssFeedToUpdate == null) {
            return false;
        }

        int index = rssFeedList.indexOf(rssFeedToUpdate);
        boolean removed = rssFeedList.remove(rssFeedToUpdate);
        if (removed) {
            rssFeedList.add(index, rssFeed);
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public synchronized boolean remove(RssFeed rssFeed) {
        if (rssFeed == null) {
            return false;
        }

        if (rssFeedList.remove(rssFeed)) {
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    private void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(FILENAME);

            for (RssFeed rssFeed : rssFeedList) {
                pw.println(rssFeed.getName() + "\t" + rssFeed.getUrl() + "\t" + rssFeed.getPeriod()
                        + "\t" + rssFeed.getLastFetchTimestamp());
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

    private void readFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Out.get().trace(e);
            }
        }

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String words[] = line.split("\t");

                if (words.length == 4) {
                    try {
                        String name = words[0];
                        String url = words[1];
                        int period = Integer.parseInt(words[2]);
                        long lastUpdate = Long.parseLong(words[3]);

                        RssFeed rssFeed = new RssFeed(name, url, period, lastUpdate);
                        rssFeedList.add(rssFeed);
                    } catch (Exception e) {
                        Out.get().trace(e);
                    }
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

}
