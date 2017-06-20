package network;

import entity.Article;
import entity.RssFeed;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import util.Out;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to encapsulate parsing of XML fetched from RSS feed.
 * Created on 6/20/17.
 *
 * @author Evgenii Kanivets
 */
public class RssFeedXmlParser {

    private static final String TAG_ITEM = "item";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PUB_DATE = "pubDate";
    private static final String TAG_LINK = "link";

    private final RssFeed rssFeed;
    private final Document document;

    public RssFeedXmlParser(RssFeed rssFeed, Document document) {
        this.rssFeed = rssFeed;
        this.document = document;
    }

    public List<Article> parse() {
        List<Article> articleList = new ArrayList<>();

        NodeList nodeList = document.getElementsByTagName(TAG_ITEM);
        if (nodeList == null || nodeList.getLength() == 0) {
            Out.get().error("Unsupported format of XML.");
        } else {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Article article = parseArticle((Element) nodeList.item(i));
                articleList.add(article);
            }
        }

        return articleList;
    }

    private Article parseArticle(Element element) {
        String title = null;
        try {
            title = element.getElementsByTagName(TAG_TITLE).item(0).getTextContent();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        String description = null;
        try {
            description = element.getElementsByTagName(TAG_DESCRIPTION).item(0).getTextContent();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        String date = null;
        try {
            date = element.getElementsByTagName(TAG_PUB_DATE).item(0).getTextContent();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        String author = null;
        try {
            author = rssFeed.getUrl();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        String link = null;
        try {
            link = element.getElementsByTagName(TAG_LINK).item(0).getTextContent();
        } catch (Exception e) {
            Out.get().trace(e);
        }

        return new Article(rssFeed.getName(), title, description, date, author, rssFeed.getUrl(), link);
    }
}
