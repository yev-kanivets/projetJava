package email;

import settings.Settings;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The main purpose of this class is to send email in case smth went wrong during fetching or parsing rss feeds.
 * <p>
 * Created by alla on 6/20/17.
 */
public class EmailManager {

    private static final String username = "scraper.idsm2016@gmail.com";
    private static final String password = "qwaszx123";

    private static EmailManager instance = new EmailManager();

    private static Properties props;

    public static EmailManager get() {
        return instance;
    }

    private EmailManager() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    public static void send(String problem) {
        if (Settings.get().getEmail() == null) return;

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Settings.get().getEmail()));
            message.setSubject("Scraper: Opps, smth went wrong during fetching or parsing rss feeds");
            message.setText("Dear " + (Settings.get().getUsername() == null ? "user" : Settings.get().getUsername()) +
                    ",\n\nThe problem appeared during the fetching of feed. \n\n " +
                    problem + "\n\nBest regards");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendStatistic(String statistic) {
        if (Settings.get().getEmail() == null) return;

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Settings.get().getEmail()));
            message.setSubject("Scraper: Daily statistic");
            message.setContent("Dear " + (Settings.get().getUsername() == null ? "user" : Settings.get().getUsername()) +
                    ",\n\nHere are the daily statistic for your instance of Scraper application. \n\n " +
                    "<pre>" + statistic + "</pre>" + "\n\nBest regards", "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
