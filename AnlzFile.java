import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AnlzFile {
	//Initialize Stuff
	File file = null;
	File file1 = null;
	FileReader fr;
	BufferedReader br = null;
	FileReader fr1;
	BufferedReader br1 = null;
	Scanner scanner = new Scanner(System.in);
	int totalWordCount = 0;
	int filterWordCount = 0;
	String pattern = null;
	Pattern filter;
	Matcher matcher;
	Pattern space = Pattern.compile("(\\S+)");
	Matcher wordMatcher;
	double ratioOfSearch = 0;
	int lineNumber = 0;
	ArrayList<String> storeLines = new ArrayList<String>();
	ArrayList<Integer> storeLineNumbers = new ArrayList<Integer>();
	ArrayList<String> storeUsers = new ArrayList<String>();
	ArrayList<String> matchedWords = new ArrayList<String>();

	//constructor that assigns a post file and a username file
	public AnlzFile(String postsfile, String usersfile) {
		file = new File(postsfile);
		file1 = new File(usersfile);
	}
	
	//constructor that defines a file
	public void defineFile(String fileName) {
		file = new File(fileName);
	}


	//the biggon
	public void userSearch() {
	
		//get ready to catch errors
		try {
			//Define variables
			String line = null; //Line that will store the lines from the first file
			String line1 = null; //Line that will store the lines from the second file
			fr = new FileReader(file); //file reader for the posts
			br = new BufferedReader(fr); //buffered reader for the posts

			if (file1 != null) {
				fr1 = new FileReader(file1); //file reader for the second file (users)
				br1 = new BufferedReader(fr1); //buffered reader for the second file (users)
			}

			boolean stay = true; //Loop variable
			String holdLine = null; //useful hold variable
			boolean stay1 = true; //second loop variable
			int holdLine1 = 0; //stores the users choice numerically
			do {
				System.out.printf("The file you are searching is %s\n", file.toString());
				System.out.println("Would you like to search for a predefined pattern? (Profanity) (Type y or n)");
				holdLine = scanner.nextLine().toLowerCase();

				//Switch through y or n for if you would like to search through a predefined pattern
				switch (holdLine) {

				case "y":
					System.out.println(
							"What would you like to search for? (Please put the number before the item you would like to search for)");
					do {
						System.out.println("[1] Profanity"); //if more options arise, I will use an arraylist to display them
						holdLine1 = scanner.nextInt();
						switch (holdLine1) {

						case 1:
							//All of the swearing is a necessary evil to search for profanity
							System.out.println("You have chosen to look for profanity in the file.");
							pattern = "(f[eu]*c*k+)|(sh+i*t+)|(cu+n+t+)|\\b-?(ass+)(ed)?-?(hole)?\\b|(di+c+k+)|(tw+a+t+)|\\b(tit)(ies)?(s)?\\b|(b[ie]+t+c+h+)|(he+ll+)(^o)?(hole)?\\b|(god)?(damn)(it)?|(wh+o+r+e)|(ni+g+er)|\\b(c[uo]+c+k+)( *[fs]u*cker)?\\b|-?(piss)-?|(bastard)(o)?|(p+u*s+y+)";
							stay1 = false; //break second loop
							stay = false;//break first loop
							break;

						default:
							System.out.println("Please display an integer!"); //if the person does not put a number repeat the loop
							break;
						}
					} while (stay1);
					break;

				case "n":
					//The user does not want to search for a predefined pattern. They would like to define their own 
					System.out.println("Please put the regular expression you would like to search for:");
					pattern = scanner.nextLine();
					stay = false;
					break;

				default:
					System.out.println("Please put 'n' or 'y'.");
					break;
				}
			} while (stay);

			filter = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

			// CONTINUE BELOW
			do {
				line = br.readLine();

				if (br1 != null) {
					line1 = br1.readLine();
				}
				storeLines.add(line);

				if (br1 != null) {
					storeUsers.add(line1);
				}

				matcher = filter.matcher(line);
				wordMatcher = space.matcher(line);

				while (wordMatcher.find()) {
					totalWordCount++;
				}

				while (matcher.find()) {
					filterWordCount++;
					if (!storeLineNumbers.contains(lineNumber)) {
						storeLineNumbers.add(lineNumber);
					}
					matchedWords.add(matcher.group());
				}
				lineNumber++;
			} while (br.ready());

			ratioOfSearch = ((double) filterWordCount / (double) totalWordCount) * 100;

			System.out.printf("Filtered Word Count: %d\nTotal Word Count: %d\nRatio of Search: %.2f%%\n",
					filterWordCount, totalWordCount, ratioOfSearch);
			// System.out.printf("The ratio of your word to the amount of words in the file
			// %s is %d to %d. This is %d%% of the whole
			// file.",file.toString(),filterWordCount,totalWordCount,ratioOfSearch);
			System.out
					.println("Would you like to see all of the lines that matched your criteria? (insert 'y' or 'n')");
			stay = true;
			do {
				holdLine = scanner.nextLine();
				switch (holdLine) {
				case "y":
					System.out.println("Would you like to output them all to a file? (insert 'y' or 'n')");
					holdLine = scanner.nextLine();

					stay1 = true;
					do {
						switch (holdLine) {
						case "n":
							printLines(storeUsers, storeLineNumbers, storeLines, br1);
							stay1 = false;
							break;

						case "y":
							printLines(storeUsers, storeLineNumbers, storeLines, br1);
							System.out.println(
									"\n\nWhat would you like the file to be called? (Please insert name.fileExtension)");
							saveLines(storeUsers, storeLineNumbers, storeLines, br1, scanner.nextLine());
							System.out.println("");
							stay1 = false;
							break;

						default:
							System.out.println("Please put 'y' or 'n'.");
							break;
						}
					} while (stay1);
					stay = false;
					break;

				case "n":
					System.out.println("Would you like to output them all to a file? (insert 'y' or 'n')");

					stay1 = true;
					do {
						switch (holdLine) {
						case "n":
							stay1 = false;
							break;

						case "y":
							System.out.println(
									"\n\nWhat would you like the file to be called? (Please insert name.fileExtension)");
							saveLines(storeUsers, storeLineNumbers, storeLines, br1, scanner.nextLine());
							stay1 = false;
							break;

						default:
							System.out.println("Please put 'y' or 'n'.");
							break;
						}
					} while (stay1);

					stay = false;
					break;

				default:
					System.out.println("Please put 'y' or 'n'.");
				}
			} while (stay);

			System.out.println("Would you like to output all of the words found to a file? (insert 'y' or 'n')");
			stay = true;
			do {
				holdLine = scanner.nextLine();
				switch (holdLine) {
				case "y":
					System.out.println("\n\nWhat would you like the file to be called?");
					saveWords(matchedWords, scanner.nextLine());
					System.out.println("Thank you for using File Analyzer!");
					stay = false;
					break;

				case "n":
					System.out.println("Ok, Thank you for using File Analyzer!");
					stay = false;
					break;

				default:
					System.out.println("Please input 'y' or 'n'.");
					break;
				}
			} while (stay);

		} catch (FileNotFoundException e) {
			System.out.printf("This file (%s) has launched this exception: %s\n", file.toString(), e.toString());
		} catch (IOException e) {
			System.out.printf("There was an input output exception\n");
		}

		try {
			br.close();
			if (br1 != null) {
				br1.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("These files did not close:" + file.toString() + " " + file1.toString());
		}

	}

	private void printLines(ArrayList<String> storeUsers, ArrayList<Integer> storeLineNumbers,
			ArrayList<String> storeLines, BufferedReader br1) {
		for (int i = 0; i < storeLineNumbers.size(); i++) {

			if (br1 != null) {
				System.out.println(Integer.toString(i + 1) + ". " + storeUsers.get(storeLineNumbers.get(i)) + " on "
						+ storeLineNumbers.get(i) + ": " + storeLines.get(storeLineNumbers.get(i)));
			} else {
				System.out.println(Integer.toString(i + 1) + ", " + storeLineNumbers.get(i) + ": "
						+ storeLines.get(storeLineNumbers.get(i)));
			}
		}
	}

	private void saveLines(ArrayList<String> storeUsers, ArrayList<Integer> storeLineNumbers,
			ArrayList<String> storeLines, BufferedReader br1, String filename) {
		FileWriter fw;
		PrintWriter pw = null;
		File file = new File(filename);

		try {
			file.createNewFile();
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.print("");
			pw.flush();

			for (int i = 0; i < storeLineNumbers.size(); i++) {
				if (br1 != null) {

					pw.printf("%4d) %15.3s on %4s: %s", (i+1),
							storeUsers.get(storeLineNumbers.get(i)), storeLineNumbers.get(i),
							storeLines.get(storeLineNumbers.get(i)));
					pw.flush();

				} else {
				
					pw.printf("%-4d) line number %+4s: %s",(i+1),storeLineNumbers.get(i),storeLines.get(storeLineNumbers.get(i)));
					pw.flush();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveWords(ArrayList<String> storeWords, String filename) {
		FileWriter fw;
		PrintWriter pw = null;
		File file = new File(filename);

		try {
			file.createNewFile();
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.print("");
			pw.flush();

			for (int i = 0; i < storeWords.size(); i++) {
				pw.println(storeWords.get(i));
				pw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
