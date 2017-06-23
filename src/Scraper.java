import command.CommandHandler;
import entity.RssFeed;
import entity.Statistic;
import network.RssFeedFetcher;
import statistic.StatisticManager;
import storage.StatisticStorage;
import storage.base.IStorage;
import storage.RssFeedStorage;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * App entry point. Main class.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class Scraper {

    public static void main(String args[]) {
        IStorage<RssFeed> rssFeedStorage = new RssFeedStorage();
        IStorage<Statistic> statisticStorage = new StatisticStorage();

        StatisticManager statisticManager = new StatisticManager(statisticStorage);
        CommandHandler commandHandler = new CommandHandler(rssFeedStorage, statisticManager);
        RssFeedFetcher rssFeedFetcher = new RssFeedFetcher(rssFeedStorage, statisticStorage);

        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(System.out, true);

        pw.println("Hi, this is the Scraper app. With help of it you can automate fetching of several rss feeds.");
        commandHandler.handleCommand(CommandHandler.CMD_HELP);

        rssFeedFetcher.start();
        statisticManager.start();

        while (!commandHandler.shouldExit()) {
            if (!commandHandler.handleCommand(sc.nextLine())) {
                pw.println("Failed to parse or execute command.");
            }
        }

        rssFeedFetcher.stop();
        statisticManager.stop();

        sc.close();
        pw.close();
    }

}
