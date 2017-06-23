package util;

import storage.TrashStorage;
import storage.base.IStorage;

import java.util.List;

/**
 * Singleton. Util class to encapsulate a logic of removing HTMl tags garbage from description fields of articles.
 * Created on 6/22/17.
 *
 * @author Evgenii Kanivets
 */
public class TextFilter {

    private static TextFilter instance;

    public static TextFilter get() {
        if (instance == null) {
            instance = new TextFilter();
        }
        return instance;
    }

    private IStorage<String> trashStorage;

    private TextFilter() {
        trashStorage = new TrashStorage();
    }

    public boolean addTrash(String trash) {
        return trashStorage.add(trash);
    }

    public List<String> getTrashList() {
        return trashStorage.getAll();
    }

    public boolean removeTrash(String trash) {
        return trashStorage.remove(trash);
    }

    public String filter(String string) {
        // replace all HTML tags with one usual space
        String result = string.replaceAll("<[^>]*>", " ");

        // Remove all rare characters like &raquo; and &nbsp;
        result = result.replaceAll("&[a-zA-Z]+;", "");

        // Replace all &#39; with ' symbol
        result = result.replaceAll("&#39;", "");

        // Remove template phrases
        for (String trash : trashStorage.getAll()) {
            result = result.replace(trash, " ");
        }

        // Replace all whitespaces with one usual space
        result = result.replaceAll("\\s+"," ");

        return result.trim();
    }

}
