package util;

/**
 * Util class to handle all the logs in one place.
 * Created on 6/20/17.
 *
 * @author Evgenii Kanivets
 */
public class Out {

    private static Out instance;

    public static Out get() {
        if (instance == null) {
            instance = new Out();
        }
        return instance;
    }

    private Out() {
    }

    public synchronized void error(String message) {
        System.out.println(message);
    }

    public synchronized void info(String message) {
        System.out.println(message);
    }

    public synchronized void trace(Exception e) {
        e.printStackTrace();
    }

}
