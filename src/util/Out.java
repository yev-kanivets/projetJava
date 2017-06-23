package util;

import email.ProblemEmailQueueManager;
import settings.Settings;

import java.io.PrintWriter;
import java.io.StringWriter;

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

    private ProblemEmailQueueManager problemEmailQueueManager;

    private Out() {
        problemEmailQueueManager = new ProblemEmailQueueManager();
    }

    public synchronized void error(String message) {
        System.out.println(message);

        if (Settings.get().getEmail() != null) {
            problemEmailQueueManager.addProblem(message);
        }
    }

    public synchronized void info(String message) {
        System.out.println(message);
    }

    public synchronized void trace(Exception e) {
        e.printStackTrace();

        if (Settings.get().getEmail() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            problemEmailQueueManager.addProblem(sw.toString());
        }
    }

}
