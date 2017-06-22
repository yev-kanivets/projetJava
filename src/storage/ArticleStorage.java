package storage;

import entity.Article;
import entity.RssFeed;
import settings.Settings;
import storage.base.BaseStorage;
import storage.base.IStorage;
import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Implementation of {@link IStorage} interface to work with {@link Article} entities.
 * Created on 6/21/17.
 *
 * @author Evgenii Kanivets
 */
public class ArticleStorage extends BaseStorage<Article> {

    private static final String DIRECTORY = "default";
    private static final String HEADER_LINE = "rss\ttitle\tdescription\tdate\tauthor\tsource\tlink";

    private File storageFile;
    private Article lastSavedArticle;

    public ArticleStorage(RssFeed rssFeed) {
        storageList = new ArrayList<>();

        initStorageFile(rssFeed);
        readFromFile();
    }

    @Override
    public synchronized boolean add(Article article) {
        if (article == null) {
            return false;
        }

        storageList.add(0, article);
        if (!storageList.isEmpty()) {
            lastSavedArticle = storageList.get(0);
        }

        saveToFile();
        return true;
    }

    @Override
    public boolean addAll(Collection<Article> collection) {
        if (collection == null) {
            return false;
        }

        if (storageList.addAll(0, collection)) {
            if (!storageList.isEmpty()) {
                lastSavedArticle = storageList.get(0);
            }

            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    public Article getLastSavedArticle() {
        return lastSavedArticle;
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

    private void initStorageFile(RssFeed rssFeed) {
        File directory = new File(DIRECTORY + File.separator + rssFeed.getName());
        if (!directory.exists()) {
            boolean directoryCreated = directory.mkdirs();
            if (directoryCreated) {
                findStorageFile(directory);
            } else {
                storageFile = null;
                Out.get().error("Failed to create a directory for rss feed " + rssFeed.getName());
            }
        } else {
            findStorageFile(directory);
        }
    }

    private void findStorageFile(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
        }

        if (files == null || files.length == 0) {
            storageFile = new File(directory, System.currentTimeMillis() + ".csv");
            putHeaderToFile();
        } else {
            findLasSavedArticle(files);
            if (files[files.length - 1].length() >= Settings.get().getFileSizeLimit()) {
                storageFile = new File(directory, System.currentTimeMillis() + ".csv");
                putHeaderToFile();
            } else {
                storageFile = files[files.length - 1];
            }
        }
    }

    private void findLasSavedArticle(File[] files) {
        for (int i = files.length - 1;i>=0;i--) {
            storageFile = files[i];
            readFromFile();
            if (storageList.isEmpty()) {
                continue;
            } else {
                lastSavedArticle = storageList.get(0);
                storageList.clear();
                break;
            }
        }
    }

}
