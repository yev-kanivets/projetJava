package statistic;

import entity.Statistic;
import storage.base.IStorage;

/**
 * Special class to manage statistic.
 * Created on 6/22/17.
 *
 * @author Evgenii Kanivets
 */
public class StatisticManager {

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
        for (int i = 0; i < maxRssLength-2; i++) {
            rss += " ";
        }

        String leftAlignFormat = "| %-" + maxRssLength + "s | %-15d | %-15d | %-16d | %-14d |%n";

        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));
        sb.append(String.format("|" + rss + "| Update requests | Failed requests | Articles fetched | Articles saved |%n"));
        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));

        for (Statistic statistic : statisticStorage.getAll()) {
            sb.append(String.format(leftAlignFormat, statistic.getRss(), statistic.getUpdateRequestCount(),
                    statistic.getFailedUpdateRequestCount(), statistic.getArticlesFetchedCount(),
                    statistic.getArticlesSavedCount()));
        }

        sb.append(String.format("+" + line + "+-----------------+-----------------+------------------+----------------+%n"));

        return sb.toString();
    }
}
