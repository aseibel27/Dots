import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Assets {
    public static String workingDir = System.getProperty("user.dir");
    public static String path = workingDir + "\\assets\\";
    public static String statsFilename = "stats.csv";
    public static ArrayList<String> stats;
    
    public static void LoadAll() {
        try {
            stats = ReadAllLines(path + statsFilename);
        } catch(Exception e) {
            System.out.println("Error: failed to read " + path + statsFilename);
        }
    }
    
    public static ArrayList<String> ReadAllLines(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File(filename);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null){
            lines.add(line);
        }
        br.close();
        fr.close();

        return lines;
    }

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
    

}