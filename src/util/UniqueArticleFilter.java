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

    private final Article lastSavedArticle;

    public UniqueArticleFilter(Article lastSavedArticle) {
        this.lastSavedArticle = lastSavedArticle;
    }

    public List<Article> filter(List<Article> articleList) {
        List<Article> result = new ArrayList<>();

        for (Article article : articleList) {
            if (article.equals(lastSavedArticle)) {
                break;
            } else {
                result.add(article);
            }
        }

        return result;
    }
}
