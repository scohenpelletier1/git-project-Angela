import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    public static void main(String[] args) throws IOException {
        initRepo();
    }
}