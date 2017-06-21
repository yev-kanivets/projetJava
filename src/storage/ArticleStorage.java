package storage;

import entity.Article;
import entity.RssFeed;
import storage.base.BaseStorage;
import storage.base.IStorage;
import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Implementation of {@link IStorage} interface to work with {@link Article} entities.
 * Created on 6/21/17.
 *
 * @author Evgenii Kanivets
 */
public class ArticleStorage extends BaseStorage<Article> {

    private static final String DIRECTORY = "default";
    private static final String HEADER_LINE = "rss\ttitle\tdescription\tdate\tauthor\tsource\tlink";

    private final File storageFile;

    public ArticleStorage(RssFeed rssFeed) {
        storageList = new ArrayList<>();

        File directory = new File(DIRECTORY + File.separator + rssFeed.getName());
        if (!directory.exists()) {
            boolean directoryCreated = directory.mkdirs();
            if (directoryCreated) {
                File[] files = directory.listFiles();
                if (files == null || files.length == 0) {
                    storageFile = new File(directory, System.currentTimeMillis() + ".csv");
                    putHeaderToFile();
                } else {
                    storageFile = files[0];
                }
            } else {
                storageFile = null;
                Out.get().error("Failed to create a directory for rss feed " + rssFeed.getName());
            }
        } else {
            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                storageFile = new File(directory, System.currentTimeMillis() + ".csv");
                putHeaderToFile();
            } else {
                storageFile = files[0];
            }
        }

        readFromFile();
    }

    private void putHeaderToFile() {
        try {
            PrintWriter pw = new PrintWriter(storageFile);

            pw.println(HEADER_LINE);

            pw.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

    @Override
    protected void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(storageFile);

            pw.println(HEADER_LINE);

            for (Article article : storageList) {
                pw.println(article.getRss() + "\t" + article.getTitle() + "\t" + article.getDescription()
                        + "\t" + article.getDate() + "\t" + article.getAuthor() + "\t" + article.getSource()
                        + "\t" + article.getLink());
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

    private void readFromFile() {
        try {
            Scanner sc = new Scanner(storageFile);

            // Skip header line
            sc.nextLine();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String words[] = line.split("\t");

                if (words.length == 7) {
                    try {
                        String rss = words[0];
                        String title = words[1];
                        String description = words[2];
                        String date = words[3];
                        String author = words[4];
                        String source = words[5];
                        String link = words[6];

                        Article article = new Article(rss, title, description, date, author, source, link);
                        storageList.add(article);
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
