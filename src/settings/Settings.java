package settings;

import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Singleton. Util class to store user specific data.
 * Created on 6/19/17.
 *
 * @author Evgenii Kanivets
 */
public class Settings {

    private static final String FILENAME = "settings.txt";

    private static Settings instance;

    public static Settings get() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private String username;
    private String email;

    private Settings() {
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
                List<String> lines = new ArrayList<>();
                while (sc.hasNextLine()) {
                    lines.add(sc.nextLine());
                }

                if (lines.size() == 2) {
                    username = lines.get(0).isEmpty() ? null : lines.get(0);
                    email = lines.get(1).isEmpty() ? null : lines.get(1);
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
            pw.println(username == null ? "" : username);
            pw.println(email == null ? "" : email);
            pw.close();
        }
    }

}
