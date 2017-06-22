package storage;

import entity.Statistic;
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
 * Implementation of {@link IStorage} interface to work with {@link Statistic} entities.
 * Created on 5/22/17.
 *
 * @author Evgenii Kanivets
 */
public class StatisticStorage extends BaseStorage<Statistic> {

    private static final String FILENAME = "statistic.txt";

    public StatisticStorage() {
        storageList = new ArrayList<>();
        readFromFile();
    }

    @Override
    public synchronized boolean update(Statistic t) {
        if (t == null) {
            return false;
        }

        Statistic statistic = null;
        for (Statistic item : storageList) {
            if (t.getRss().equals(item.getRss())) {
                statistic = item;
            }
        }

        if (statistic == null) {
            return false;
        }

        int index = storageList.indexOf(statistic);
        boolean removed = storageList.remove(statistic);
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

            for (Statistic statistic : storageList) {
                pw.println(statistic.getRss() + "\t" + statistic.getUpdateRequestCount() + "\t"
                        + statistic.getFailedUpdateRequestCount() + "\t" + statistic.getArticlesFetchedCount()
                        + "\t" + statistic.getArticlesSavedCount());
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

                if (words.length == 5) {
                    try {
                        String rss = words[0];
                        int updateRequestCount = Integer.parseInt(words[1]);
                        int failedUpdateRequestCount = Integer.parseInt(words[2]);
                        int articlesFetchedCount = Integer.parseInt(words[3]);
                        int articlesSavedCount = Integer.parseInt(words[4]);

                        Statistic statistic = new Statistic(rss, updateRequestCount, failedUpdateRequestCount,
                                articlesFetchedCount, articlesSavedCount);
                        storageList.add(statistic);
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
