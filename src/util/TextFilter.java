package util;

/**
 * Util class to encapsulate a logic of removing HTMl tags garbage from description fields of articles.
 * Created on 6/22/17.
 *
 * @author Evgenii Kanivets
 */
public class TextFilter {

    public static String filter(String string) {
        // replace all HTML tags with one usual space
        String result = string.replaceAll("<[^>]*>", " ");

        // Remove all rare characters like &raquo; and &nbsp;
        result = result.replaceAll("&[a-zA-Z]+;", "");

        // Replace all &#39; with ' symbol
        result = result.replaceAll("&#39;", "");

        // Replace all whitespaces with one usual space
        result = result.replaceAll("\\s+"," ");

        return result.trim();
    }

}
