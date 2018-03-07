public class Main {
	public static void main(String[] args) {
		AnlzFile redditPosts = new AnlzFile("redditPosts.txt","redditAutors.txt");
		//AnlzFile testFile = new AnlzFile("testfile.txt");
		redditPosts.userSearch();
		//testFile.userSearch();
	}
}
