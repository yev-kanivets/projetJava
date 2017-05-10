package entity;

/**
 * Base entity class to encapsulate common methods.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class BaseEntity {

    public static boolean isEquals(String str1, String str2) {
        if (str1 == null) {
            return str2 != null;
        } else {
            return str1.equals(str2);
        }
    }

}
