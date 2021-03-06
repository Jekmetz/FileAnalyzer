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
	ArrayList<Integer> storeLineNumbersLines = new ArrayList<Integer>();
	ArrayList<Integer> storeLineNumbersWords = new ArrayList<Integer>();
	ArrayList<String> storeUsers = new ArrayList<String>();
	ArrayList<String> matchedWords = new ArrayList<String>();
	String insertLineNumbers = null;
	String tempFileName = null;

	//constructor that assigns a post file and a username file
	public AnlzFile(String postsfile, String usersfile) {
		file = new File(postsfile);
		file1 = new File(usersfile);
	}
	
	//constructor that defines a file
	public AnlzFile(String fileName) {
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
							pattern = "(f[eu]*c*k+)(i*ng)?(ed)?|(bull)?(sh+i*t+)(poster)?(pool)?|(cu+n+t+)|\\b-?(ass+)(ed)?-?(hole)?\\b|(slap)?(di+c+k+)|(tw+a+t+)|\\b(tit)(ies)?(s)?\\b|(b[ie]+t+c+h+)|(he+ll+)(^o)?(hole)?\\b|(god)?(damn)(it)?|(wh+o+r+e)|(ni+g+er)|\\b(c[uo]+c+k+)( *[fs]u*cker)?\\b|-?(piss)-?|(bastard)(o)?|(p+u*s+y+)|\\b(raped?)";
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
					if (!storeLineNumbersLines.contains(lineNumber)) {
						storeLineNumbersLines.add(lineNumber);
					}
					matchedWords.add(matcher.group());
					storeLineNumbersWords.add(lineNumber);
				}
				lineNumber++;
			} while (br.ready());

			ratioOfSearch = ((double) filterWordCount / (double) totalWordCount) * 100;

			System.out.printf("Filtered Word Count: %d\nTotal Word Count: %d\nRatio of Search: %.2f%%\n",
					filterWordCount, totalWordCount, ratioOfSearch);
			// System.out.printf("The ratio of your word to the amount of words in the file
			// %s is %d to %d. This is %d%% of the whole
			// file.",file.toString(),filterWordCount,totalWordCount,ratioOfSearch);
			System.out.println("Would you like to see all of the lines that matched your criteria? (insert 'y' or 'n')");
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
							printLines(storeUsers, storeLineNumbersLines, storeLines, br1);
							stay1 = false;
							break;

						case "y":
							printLines(storeUsers, storeLineNumbersLines, storeLines, br1);
							System.out.println("\n\nWhat would you like the file to be called? (Please insert name.fileExtension)");
							tempFileName = scanner.nextLine();
							saveLines(storeUsers, storeLineNumbersLines, storeLines, br1, tempFileName);
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
					holdLine = scanner.nextLine();

					stay1 = true;
					do {
						switch (holdLine) {
						case "n":
							stay1 = false;
							break;

						case "y":
							System.out.println("\n\nWhat would you like the file to be called? (Please insert name.fileExtension)");
							tempFileName = scanner.nextLine();
							saveLines(storeUsers, storeLineNumbersLines, storeLines, br1, tempFileName);
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
					System.out.println("\nWould you like to include line numbers in this file? (insert 'y' or 'n')");
					insertLineNumbers = scanner.nextLine();
					System.out.println("\n\nWhat would you like the file to be called?");
					tempFileName = scanner.nextLine();
					saveWords(matchedWords, storeLineNumbersWords, tempFileName,insertLineNumbers);
					saveStats(filterWordCount, totalWordCount, ratioOfSearch, tempFileName, pattern);
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
			System.out.println("These files did not close:" + file.toString() + " " + file1.toString());
		}

	}

	private void printLines(ArrayList<String> storeUsers, ArrayList<Integer> storeLineNumbersLines,
			ArrayList<String> storeLines, BufferedReader br1) {
		for (int i = 0; i < storeLineNumbersLines.size(); i++) {

			if (br1 != null) {
				System.out.printf("%4d) %15.15s on %4s: %s\n", (i+1),
							storeUsers.get(storeLineNumbersLines.get(i)), storeLineNumbersLines.get(i),
							storeLines.get(storeLineNumbersLines.get(i)));
			} else {
				System.out.printf("%4d) line number %4s: %s\n",(i+1),storeLineNumbersLines.get(i),storeLines.get(storeLineNumbersLines.get(i)));
			}
		}
	}

	private void saveLines(ArrayList<String> storeUsers, ArrayList<Integer> storeLineNumbersLines,
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
	
				for (int i = 0; i < storeLineNumbersLines.size(); i++) {
					if (br1 != null) {
						
						pw.println(Integer.toString(i+1) + ") " + storeUsers.get(storeLineNumbersLines.get(i)) +
							       	" on " + Integer.toString(storeLineNumbersLines.get(i))
								       	+ ": " + storeLines.get(storeLineNumbersLines.get(i)));
						//pw.printf("%4d) %15.3s on %4s: %s\n", (i+1),
						//		storeUsers.get(storeLineNumbersLines.get(i)), storeLineNumbersLines.get(i),
						//		storeLines.get(storeLineNumbersLines.get(i)));
						pw.flush();
	
					} else {
						
						
						pw.println(Integer.toString(i+1) + ") " + " on " + Integer.toString(storeLineNumbersLines.get(i))
							       	+ ": " + storeLines.get(storeLineNumbersLines.get(i)));	
						//pw.printf("%-4d) line number %+4s: %s\n",(i+1),storeLineNumbersLines.get(i),storeLines.get(storeLineNumbersLines.get(i)));
						pw.flush();
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveWords(ArrayList<String> storeWords,ArrayList<Integer> storeLineNumbersWords, String filename, String insertLineNumbers) {
		FileWriter fw;
		PrintWriter pw = null;
		File file = new File(filename);

		try {
			file.createNewFile();
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.print("");
			pw.flush();
			switch(insertLineNumbers){
				case "y":
				for (int i = 0; i < storeWords.size(); i++) {
					pw.println(Integer.toString(storeLineNumbersWords.get(i)) + ") " + storeWords.get(i));				
					//pw.printf("%-4d) %s\n",storeLineNumbersWords.get(i),storeWords.get(i));
					pw.flush();
				}
				break;

				default:
				for (int i = 0; i < storeWords.size(); i++) {
					pw.println(storeWords.get(i));				
					//pw.printf("%-4d) %s\n",storeLineNumbersWords.get(i),storeWords.get(i));
					pw.flush();
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveStats(int filterWordCount, int totalWordCount, double ratioOfSearch, String filename, String pattern){
		FileWriter fw;
		PrintWriter pw = null;
		File file = new File(filename.substring(0,filename.length()-4) + "stats" + filename.substring(filename.length()-4,filename.length()));
		
		try{
			file.createNewFile();
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.print("");
			pw.flush();
			pw.println("File that was searched: " + filename);
			pw.flush();
			pw.println("Pattern that was searched: " + pattern);
			pw.flush();
			pw.println("Filtered Word Count: " + Integer.toString(filterWordCount));
			pw.flush();
			pw.println("Total Word Count: " + Integer.toString(totalWordCount));
			pw.flush();
			pw.println("Ratio of Search: " + Double.toString(ratioOfSearch) + "%");
			pw.flush();
				
	//			("Filtered Word Count: %d\nTotal Word Count: %d\nRatio of Search: %.2f%%\n",
	//				filterWordCount, totalWordCount, ratioOfSearch)
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
