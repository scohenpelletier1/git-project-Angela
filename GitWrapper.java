import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;

public class GitWrapper {

    /**
     * Initializes a new Git repository.
     * This method creates the necessary directory structure
     * and initial files (index, HEAD) required for a Git repository.
     * @throws IOException 
     */
    public void init() throws IOException {
        Git.initRepo();

    }

    /**
     * Stages a file for the next commit.
     * This method adds a file to the index file.
     * If the file does not exist, it throws an IOException.
     * If the file is a directory, it throws an IOException.
     * If the file is already in the index, it does nothing.
     * If the file is successfully staged, it creates a blob for the file.
     * @param filePath The path to the file to be staged.
     * @throws IOException 
    */
    public void add(String filePath) throws IOException {
        Git.blob(filePath);
        Git.addToIdx(Git.hashFile(filePath), filePath);

    }

    /**
     * Creates a commit with the given author and message.
     * It should capture the current state of the repository by building trees based on the index file,
     * writing the tree to the objects directory,
     * writing the commit to the objects directory,
     * updating the HEAD file,
     * and returning the commit hash.
     * 
     * The commit should be formatted as follows:
     * tree: <tree_sha>
     * parent: <parent_sha>
     * author: <author>
     * date: <date>
     * summary: <summary>
     *
     * @param author  The name of the author making the commit.
     * @param message The commit message describing the changes.
     * @return The SHA1 hash of the new commit.
     * @throws IOException 
    */
    public String commit(String author, String message) throws IOException {
        // create the string builder
        StringBuilder commitMessage = new StringBuilder();
        
        // add to the commit message
        commitMessage.append("tree: " + Git.genTreesFromIdx() + "\n");
        commitMessage.append("parent: " + Git.getLastCommit() + "\n");
        
        // update commit message
        commitMessage.append("author: " + author + "\n");

        // timeee
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);

        LocalDate currentDate = LocalDate.parse(year + "-" + month + "-" + day);
        int currentDay = currentDate.getDayOfMonth();
        String currentMonth = currentDate.getMonth().toString().substring(0, 1) + currentDate.getMonth().toString().toLowerCase().substring(1, 3);
        int currentYear = currentDate.getYear();

        commitMessage.append("date: " + currentMonth + " " + currentDay + ", " + currentYear + "\n");

        // input message
        commitMessage.append("summary: " + message);

        // create commit file
        File commitFile = new File("tempName");
        Path tempName = Paths.get("tempName");

        // write message
        BufferedWriter writer = new BufferedWriter(new FileWriter(commitFile));
        writer.write(commitMessage.toString());
        writer.close();

        // update the commit file path
        String hash = Git.hashFile(commitFile.getPath());
        Path newName = Paths.get("git/objects/" + hash);

        Files.move(tempName, newName, StandardCopyOption.REPLACE_EXISTING);

        // update HEAD file
        Git.updateHead(new File(hash));

        return hash;
    }

    /**
     * EXTRA CREDIT:
     * Checks out a specific commit given its hash.
     * This method should read the HEAD file to determine the "checked out" commit.
     * Then it should update the working directory to match the
     * state of the repository at that commit by tracing through the root tree and
     * all its children.
     *
     * @param commitHash The SHA1 hash of the commit to check out.
     * @throws Exception 
    */
    public void checkout(String commitHash) throws Exception {
        // I was super confused by the instructions and how to even tackle this method, so after working on this for about 4 hours (deleted a lot of code that didn't work), I ended up having to move on to other homework and submit this assignment.

        // get the commit file
        File commitFile = new File("git/objects", commitHash);

        // exception handling
        if (!commitFile.exists()) {
            throw new FileNotFoundException("This commit does not exist");

        }

        // find the tree
        BufferedReader reader = new BufferedReader(new FileReader(commitFile));
        String treeHash = "";
        String line;

        while ((line = reader.readLine()) != null) {
            // if you find the line 
            if (line.startsWith("tree: ")) {
                // then get the hash
                treeHash = line.substring(6, 46).trim();
                break;

            }

        }

        reader.close();

        // exception handling (again)
        if (treeHash == null) {
            throw new FileNotFoundException("This commit does not have a tree");
        
        }

    }

}