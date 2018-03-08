public class Main {
	public static void main(String[] args) {
		AnlzFile redditPosts = new AnlzFile("redditPosts.txt","redditAutors.txt");
		//AnlzFile wordtest = new AnlzFile("wordtest.txt");
		//AnlzFile testFile = new AnlzFile("testfile.txt");
		redditPosts.userSearch();
		//testFile.userSearch();
		//wordtest.userSearch();
	}
}
