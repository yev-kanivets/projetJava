package util;

import entity.Article;

import java.util.*;

/**
 * Util class to filter articles that are already fetched.
 * Created on 6/21/17.
 *
 * @author Evgenii Kanivets
 */
public class UniqueArticleFilter {

    private final Set<Article> articleSet;

    public UniqueArticleFilter(Collection<Article> articleCollection) {
        articleSet = new HashSet<>(articleCollection);
    }

    public List<Article> filter(List<Article> articleList) {
        List<Article> result = new ArrayList<>();

        for (Article article : articleList) {
            if (!articleSet.contains(article)) {
                result.add(article);
            }
        }

        return result;
    }
}
