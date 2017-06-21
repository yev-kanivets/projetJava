package email;

import settings.Settings;
import util.Out;

import java.text.DateFormat;
import java.util.*;

/**
 * Manager class to stuck several continuous errors and send in one email.
 * Created on 6/21/17.
 *
 * @author Evgenii Kanivets
 */
public class EmailQueueManager {

    private static final int SEND_DELAY = 10000;
    private static final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

    private Timer timer;
    private final List<String> emailQueue;

    public EmailQueueManager() {
        emailQueue = new ArrayList<>();
    }

    public synchronized void addProblem(String problem) {
        emailQueue.add(format.format(new Date()) + " " + problem);

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (emailQueue.isEmpty()) {
                    return;
                }
                EmailManager.send(getMessage());
            }
        }, SEND_DELAY);
    }

    public synchronized String getMessage() {
        StringBuilder sb = new StringBuilder();

        for (String string : emailQueue) {
            sb.append(string).append("\n\n");
        }

        Out.get().info("Sent " + emailQueue.size() + " problems to " + Settings.get().getEmail());
        emailQueue.clear();

        return sb.toString();
    }

}
