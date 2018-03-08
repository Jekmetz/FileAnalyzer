Make sure you have all of the files in the same folder please.
This is a File analyzer. All one has to do is change the file in the java to the file they would like to search.
By default, it searches redditPosts.txt.

If you would like to change the file searched, you must comment out every line except the ones that start with 'public'
or '}' and then between the brackets, type:

	AnlzFile newObject = new AnlzFile("fileToBeSearched.fileExtension");
	newObject.userSearch();

where:

newObject > Any word you see fit
fileToBeSearched > the name of the file to be searched
fileExtension > the extension of the file to be searched (usually txt)

Happy Hunting!
J
