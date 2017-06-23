package storage;

import storage.base.BaseStorage;
import storage.base.IStorage;
import util.Out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Implementation of {@link IStorage} interface to work with trash (usual string) entities.
 * Created on 6/23/17.
 *
 * @author Evgenii Kanivets
 */
public class TrashStorage extends BaseStorage<String> {

    private static final String FILENAME = "trash.txt";

    public TrashStorage() {
        storageList = new ArrayList<>();
        readFromFile();
    }

    @Override
    protected void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(FILENAME);

            for (String string : storageList) {
                pw.println(string);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

    private void readFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Out.get().trace(e);
            }
        }

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                storageList.add(line);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            Out.get().trace(e);
        }
    }

}
