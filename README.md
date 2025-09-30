# git-project-Angela
Call the initRepo() method in the main to initialize a git directory and an objects directory, HEAD file, and index file inside it.
You can initialize, verify, and cleanup the repository through calling methods to test if it can be created and deleted. 
A SHA-1 hash function was created to create a hexadecimal string representing the filpath
To create BLOBs call the blob() method on any file you want to track, creating a file in the objects directory named after the file’s SHA-1 hash and containing the content of your original file.
To add files to the index use addToIdx(hashFile("path/to/your/file"), "filename.txt"). 
To reset or clean up the repository for testing, reset() clears everything in the objects foler and refreshes index, while cleanUp() removes the entire git folder.
the tester will initialize the repo if it doesn’t exist, create sample text files, blob them & add entries to index file, and reset.