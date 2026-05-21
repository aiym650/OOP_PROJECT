package university.patterns;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final String FILE = "university_data.ser";

    private DataStorage() {}

    public static void save(List<?> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE))) {
            oos.writeObject(data);
            System.out.println("[Storage] Data saved successfully.");
        } catch (IOException e) {
            System.out.println("[Storage] Save error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> load() {
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE))) {
            List<T> data = (List<T>) ois.readObject();
            System.out.println("[Storage] Loaded " + data.size() + " records.");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[Storage] Could not load data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean hasSavedData() {
        return new File(FILE).exists();
    }

    public static void delete() {
        if (new File(FILE).delete())
            System.out.println("[Storage] Saved data cleared.");
    }
}