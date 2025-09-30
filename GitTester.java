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

        File sampleDir = new File("git/samples");
        sampleDir.mkdirs();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("git/samples/file1.txt"))) {
            bufferedWriter.write("Hello world");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("git/samples/file2.txt"))) {
            bufferedWriter.write("Hello world again");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("git/samples/file3.txt"))) {
            bufferedWriter.write("Hello world again again");
        }
        blob("git/samples/file1.txt");
        blob("git/samples/file2.txt");
        blob("git/samples/file3.txt");
        addToIdx(hashFile("git/samples/file1.txt"), "git/samples/file1.txt");
        addToIdx(hashFile("git/samples/file2.txt"), "git/samples/file2.txt");
        addToIdx(hashFile("git/samples/file3.txt"), "git/samples/file3.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("git/index"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] s = line.split(" ", 2);
                if (s.length == 2) {
                    String hash = s[0];
                    File blob = new File("git/objects", hash);
                    if (blob.exists()) System.out.println("yess");
                    else System.out.println("nooo");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}