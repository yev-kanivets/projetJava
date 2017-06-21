package entity;

/**
 * Entity class for an article from RSS channel.
 * Created on 6/20/17.
 *
 * @author Evgenii Kanivets
 */
public class Article extends BaseEntity {

    private final String rss;
    private final String title;
    private final String description;
    private final String date;
    private final String author;
    private final String source;
    private final String link;

    public Article(String rss, String title, String description, String date, String author, String source, String link) {
        this.rss = rss;
        this.title = title;
        this.description = description;
        this.date = date;
        this.author = author;
        this.source = source;
        this.link = link;
    }

    public String getRss() {
        return rss;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Article{" +
                "rss='" + rss + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Article) {
            Article article = (Article) obj;
            return isEquals(rss, article.getRss())
                    && isEquals(title, article.getTitle())
                    && isEquals(date, article.getDate())
                    && isEquals(author, article.getAuthor())
                    && isEquals(source, article.getSource());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = rss != null ? rss.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
