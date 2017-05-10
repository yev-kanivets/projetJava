import command.CommandHandler;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * App entry point. Main class.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class Scraper {

    public static void main(String args[]) {
        CommandHandler commandHandler = new CommandHandler();

        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(System.out, true);

        pw.println("Hi, this is the Scraper app. With help of it you can automate fetching of several rss feeds.");
        commandHandler.handleCommand(CommandHandler.CMD_HELP);

        while (!commandHandler.shouldExit()) {
            if (!commandHandler.handleCommand(sc.nextLine())) {
                pw.println("Failed to parse or execute command.");
            }
        }

        sc.close();
        pw.close();
    }

}
