package statistic;

import email.EmailManager;
import entity.Statistic;
import storage.base.IStorage;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Special class to manage statistic.
 * Created on 6/22/17.
 *
 * @author Evgenii Kanivets
 */
public class StatisticManager {

    private static final int DAY_PERIOD = 24 * 60 * 60 * 1000;

    private final IStorage<Statistic> statisticStorage;

    public StatisticManager(IStorage<Statistic> statisticStorage) {
        this.statisticStorage = statisticStorage;
    }

    public String getStatisticMessage() {
        StringBuilder sb = new StringBuilder();

        int maxRssLength = 10;
        for (Statistic statistic : statisticStorage.getAll()) {
            maxRssLength = Math.max(maxRssLength, statistic.getRss().length());
        }

        String line = "--";
        for (int i = 0; i < maxRssLength; i++) {
            line += "-";
        }

        String rss = " RSS";
        for (int i = 0; i < maxRssLength - 2; i++) {
            rss += " ";
        }

        String leftAlignFormat = "| %-" + maxRssLength + "s | %-15d | %-15d | %-16d | %-14d |%n";

        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));
        sb.append(String.format("|" + rss + "| Update requests | Failed requests | Articles fetched | Articles saved |%n"));
        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));

        int updateRequestSum = 0;
        int failedUpdateRequestSum = 0;
        int articlesFetchedSum = 0;
        int articlesSavedSum = 0;

        for (Statistic statistic : statisticStorage.getAll()) {
            updateRequestSum += statistic.getUpdateRequestCount();
            failedUpdateRequestSum += statistic.getFailedUpdateRequestCount();
            articlesFetchedSum += statistic.getArticlesFetchedCount();
            articlesSavedSum += statistic.getArticlesSavedCount();

            sb.append(String.format(leftAlignFormat, statistic.getRss(), statistic.getUpdateRequestCount(),
                    statistic.getFailedUpdateRequestCount(), statistic.getArticlesFetchedCount(),
                    statistic.getArticlesSavedCount()));
        }

        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));

        sb.append(String.format(leftAlignFormat, "Total", updateRequestSum, failedUpdateRequestSum, articlesFetchedSum,
                articlesSavedSum));

        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));

        return sb.toString();
    }

    public void start() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.HOUR, 24);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EmailManager.sendStatistic(getStatisticMessage());
                // Clear the Queue
                for (Statistic statistic : statisticStorage.getAll()) {
                    statisticStorage.remove(statistic);
                }
            }
        }, calendar.getTime(), DAY_PERIOD);
    }
}
