import java.io.*;

public class GitTester extends Git {

    public static boolean verify() {
        File f1 = new File("git");
        File f2 = new File("git/objects");
        File f3 = new File("git/index");
        File f4 = new File("git/HEAD");
        if(f1.exists() && f2.exists() && f3.exists() && f4.exists()) return true;
        return false;
    }

    public static void cleanUp() {
        File f1 = new File("git");
        File f2 = new File("git/objects");
        File f3 = new File("git/index");
        File f4 = new File("git/HEAD");
        f2.delete();
        f3.delete();
        f4.delete();
        f1.delete();
    }
    public static void main(String[] args) throws IOException {
        initRepo();
        System.out.println(verify());
        cleanUp();
    }
}