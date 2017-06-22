package settings;

import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Singleton. Util class to store user specific data.
 * Created on 6/19/17.
 *
 * @author Evgenii Kanivets
 */
public class Settings {

    private static final String FILENAME = "settings.txt";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FILE_SIZE_LIMIT = "file_size_limit";

    private static final int DEFAULT_FILE_SIZE_LIMIT = 1024 * 1024; // 1 MB

    private static Settings instance;

    public static Settings get() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private String username;
    private String email;
    private long fileSizeLimit;

    private Settings() {
        username = null;
        email = null;
        fileSizeLimit = DEFAULT_FILE_SIZE_LIMIT;

        readFromFile();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        saveToFile();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        saveToFile();
    }

    public long getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
        saveToFile();
    }

    private void readFromFile() {
        File file = new File(FILENAME);

        if (file.exists()) {
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                Out.get().trace(e);
            }

            if (sc != null) {
                Map<String, String> map = new TreeMap<>();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] words = line.split("=");
                    if (words.length == 2) {
                        map.put(words[0], words[1]);
                    }
                }

                username = map.get(KEY_USERNAME);
                email = map.get(KEY_EMAIL);
                try {
                    fileSizeLimit = Long.parseLong(map.get(KEY_FILE_SIZE_LIMIT));
                } catch (Exception e) {
                    Out.get().trace(e);
                }

                sc.close();
            } else {
                Out.get().error("Failed to read username and email from " + FILENAME);
            }
        } else {
            try {
                boolean newFileCreated = file.createNewFile();
                if (newFileCreated) {
                    Out.get().info("Created " + FILENAME);
                } else {
                    Out.get().error("Failed to create " + FILENAME);
                }
            } catch (IOException e) {
                Out.get().trace(e);
            }
        }
    }

    private void saveToFile() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(FILENAME);
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }

        if (pw != null) {
            pw.println(KEY_USERNAME + "=" + (username == null ? "" : username));
            pw.println(KEY_EMAIL + "=" + (email == null ? "" : email));
            pw.println(KEY_FILE_SIZE_LIMIT + "=" + fileSizeLimit);
            pw.close();
        }
    }

}
