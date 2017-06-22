package command;

import com.sun.deploy.util.StringUtils;
import entity.RssFeed;
import settings.Settings;
import storage.base.IStorage;
import util.Out;

import java.util.Arrays;

/**
 * Util class that handles all the commands entered from command line.
 * Created on 5/10/17.
 *
 * @author Evgenii Kanivets
 */
public class CommandHandler {

    public static final String CMD_HELP = "help";
    public static final String CMD_EXIT = "exit";

    public static final String CMD_ADD = "add";
    public static final String CMD_LIST = "list";
    public static final String CMD_UPDATE = "update";
    public static final String CMD_REMOVE = "remove";

    public static final String CMD_SET = "set";
    public static final String CMD_PRINT = "print";
    public static final String CMD_USERNAME = "username";
    public static final String CMD_EMAIL = "email";
    public static final String CMD_FILE_SIZE_LIMIT = "file_size_limit";

    public static final String CMD_FETCH = "fetch";

    private boolean shouldExit;
    private IStorage<RssFeed> rssFeedStorage;

    public CommandHandler(IStorage<RssFeed> rssFeedStorage) {
        this.rssFeedStorage = rssFeedStorage;
    }

    /**
     * @param command that were entered from CLI
     * @param command that were entered from CLI
     * @return true if command was handled
     */
    public boolean handleCommand(String command) {
        if (command == null) {
            return false;
        }

        String[] words = command.split(" ");

        String[] params = null;
        if (words.length > 1) {
            params = Arrays.copyOfRange(words, 1, words.length);
        }

        switch (words[0]) {
            case CMD_HELP:
                return help(params);

            case CMD_EXIT:
                return exit(params);

            case CMD_ADD:
                return add(params);

            case CMD_LIST:
                return list(params);

            case CMD_UPDATE:
                return update(params);

            case CMD_REMOVE:
                return remove(params);

            case CMD_SET:
                return set(params);

            case CMD_PRINT:
                return print(params);

            case CMD_FETCH:
                return fetch(params);

            default:
                return false;
        }
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    private boolean help(String[] params) {
        Out.get().info("Here are options:\n" +
                " - add RSS link with <name> <link> <period of check in second> (optional). Example: 'add rss1 http://google.com'\n" +
                " - update RSS link with <name> <link> <period of check in second> (optional). Example: 'update rss1 http://google.com'\n" +
                " - list added RSS links. Example: 'list'\n" +
                " - remove RSS link by name. Example: 'remove rss1'\n" +
                " - set username. Example: 'set username Test'\n" +
                " - set email. Example 'set email test@gmail.com'\n" +
                " - set file size limit in bytes. Example 'set file_size_limit 1048576'\n" +
                " - print username. Example: 'print username'\n" +
                " - print email. Example 'print email'\n" +
                " - print file size limit. Example 'print file_size_limit'\n" +
                " - fetch RSS feed from all links. Example 'fetch'\n" +
                " - help, print these options once more time");
        return true;
    }

    private boolean exit(String[] params) {
        shouldExit = true;
        return true;
    }

    private boolean add(String[] params) {
        if (params == null || params.length < 2) {
            return false;
        }

        String name = params[0];
        String url = params[1];
        int period = RssFeed.DEFAULT_PERIOD;

        if (params.length > 2) {
            try {
                period = Integer.parseInt(params[2]);
            } catch (Exception e) {
                return false;
            }
        }

        RssFeed rssFeed = new RssFeed(name, url, period);
        rssFeedStorage.add(rssFeed);

        return true;
    }

    private boolean list(String[] params) {
        for (RssFeed rssFeed : rssFeedStorage.getAll()) {
            Out.get().info(rssFeed.toString());
        }
        return true;
    }

    private boolean update(String[] params) {
        if (params == null || params.length < 2) {
            return false;
        }

        String name = params[0];
        String url = params[1];
        int period = RssFeed.DEFAULT_PERIOD;

        if (params.length > 2) {
            try {
                period = Integer.parseInt(params[2]);
            } catch (Exception e) {
                return false;
            }
        }

        RssFeed rssFeed = new RssFeed(name, url, period);
        return rssFeedStorage.update(rssFeed);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean remove(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        String name = params[0];
        RssFeed rssFeedToRemove = null;
        for (RssFeed rssFeed : rssFeedStorage.getAll()) {
            if (name.equals(rssFeed.getName())) {
                rssFeedToRemove = rssFeed;
            }
        }

        if (rssFeedToRemove == null) {
            return false;
        } else {
            return rssFeedStorage.remove(rssFeedToRemove);
        }
    }

    private boolean set(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        switch (params[0]) {
            case CMD_USERNAME:
                return setUsername(Arrays.copyOfRange(params, 1, params.length));

            case CMD_EMAIL:
                return setEmail(Arrays.copyOfRange(params, 1, params.length));

            case CMD_FILE_SIZE_LIMIT:
                return setFileSizeLimit(Arrays.copyOfRange(params, 1, params.length));

            default:
                return false;
        }
    }

    private boolean print(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        switch (params[0]) {
            case CMD_USERNAME:
                Out.get().info(Settings.get().getUsername());
                return true;

            case CMD_EMAIL:
                Out.get().info(Settings.get().getEmail());
                return true;

            case CMD_FILE_SIZE_LIMIT:
                Out.get().info(Long.toString(Settings.get().getFileSizeLimit()));
                return true;

            default:
                return false;
        }
    }

    private boolean setUsername(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        String username = StringUtils.join(Arrays.asList(params), " ");
        Settings.get().setUsername(username);
        return true;
    }

    private boolean setEmail(String[] params) {
        if (params == null || params.length < 1) {
            return false;
        }

        String email = StringUtils.join(Arrays.asList(params), " ");
        Settings.get().setEmail(email);
        return true;
    }

    private boolean setFileSizeLimit(String[] params) {
        if (params == null || params.length != 1) {
            return false;
        }

        long fileSizeLimit = -1;
        try {
            fileSizeLimit = Long.parseLong(params[0]);
        } catch (Exception e) {
            Out.get().trace(e);
        }

        if (fileSizeLimit == -1) {
            return false;
        } else {
            Settings.get().setFileSizeLimit(fileSizeLimit);
        }

        return true;
    }

    private boolean fetch(String[] params) {
        // Just reset las fetched timestamp to 0
        for (RssFeed rssFeed : rssFeedStorage.getAll()) {
            RssFeed newRssFeed = new RssFeed(rssFeed.getName(), rssFeed.getUrl(), rssFeed.getPeriod(), 0);
            rssFeedStorage.update(newRssFeed);
        }
        return true;
    }

}
