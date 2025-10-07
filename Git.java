import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Git {

    public static void initRepo() throws IOException {
        File f1 = new File("git");
        File f2 = new File("git/objects");
        File f3 = new File("git/index");
        File f4 = new File("git/HEAD");
        if(f1.exists() && f2.exists() && f3.exists() && f4.exists()) System.out.println("Git Repository Already Exists");
        else System.out.println("Git Repository Created");
        if(!f1.exists()) f1.mkdir();
        if(!f2.exists()) f2.mkdir();
        if(!f3.exists()) f3.createNewFile();
        if(!f4.exists()) f4.createNewFile();
    }

    public static String hashFile(String filepath) {
        try (FileInputStream f = new FileInputStream(filepath)) {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bt = new byte[8192];
            int i;
            while ((i = f.read(bt)) != -1) {
                md.update(bt, 0, i);
            }
            byte[] hashedBytes = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found " + filepath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: SHA-256 algorithm not available.");
            e.printStackTrace();
        }
        return null;
    }

    public static void blob(String filepath) throws IOException {
        File f1 = new File(filepath);
        String hash = hashFile(filepath);
        File f2 = new File("git/objects/" + hash);
        f2.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f2))) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f1))) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(s);
                    bufferedWriter.newLine();
                }
            }
        }
    }

    public static void addToIdx(String hash, String filepath) {
        try {
            File f = new File("git/index");
            if (!f.exists()) f.createNewFile();
            String path = new File(filepath).getPath();
            List<String> lines = new ArrayList<>();
            boolean found = false;
            boolean updated = false;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(" ", 2);
                    if (parts.length == 2) {
                        String oldHash = parts[0];
                        String oldPath = parts[1];

                        if (oldPath.equals(path)) {
                            found = true;
                            if (!oldHash.equals(hash)) {
                                lines.add(hash + " " + path);
                                updated = true;
                            } else {
                                lines.add(line);
                            }
                        } else {
                            lines.add(line);
                        }
                    }
                }
            }
            if (!found) {
                lines.add(hash + " " + path);
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f))) {
                for (int i = 0; i < lines.size(); i++) {
                    bufferedWriter.write(lines.get(i));
                    if (i < lines.size() - 1) bufferedWriter.newLine();
                }
            }
            File b = new File("git/objects", hash);
            if (!b.exists() || updated) {
                blob(filepath);
                System.out.println("blob made/updated for " + path);
            } else {
                System.out.println("duplicate skipped for " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        initRepo();
        addToIdx(hashFile("git/testFile"), "git/testFile");
        addToIdx(hashFile("git/samples/file1.txt"), "git/samples/file1.txt");
    }
}