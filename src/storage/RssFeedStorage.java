package storage;

import entity.RssFeed;
import storage.base.BaseStorage;
import storage.base.IStorage;
import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Implementation of {@link IStorage} interface to work with {@link RssFeed} entities.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedStorage extends BaseStorage<RssFeed> {

    private static final String FILENAME = "default.cfg";

    public RssFeedStorage() {
        storageList = new ArrayList<>();
        readFromFile();
    }

    @Override
    public synchronized boolean update(RssFeed t) {
        if (t == null) {
            return false;
        }

        RssFeed rssFeedToUpdate = null;
        for (RssFeed item : storageList) {
            if (t.getName().equals(item.getName())) {
                rssFeedToUpdate = item;
            }
        }

        if (rssFeedToUpdate == null) {
            return false;
        }

        int index = storageList.indexOf(rssFeedToUpdate);
        boolean removed = storageList.remove(rssFeedToUpdate);
        if (removed) {
            storageList.add(index, t);
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(FILENAME);

            for (RssFeed rssFeed : storageList) {
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
                        storageList.add(rssFeed);
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
