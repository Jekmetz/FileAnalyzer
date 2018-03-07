import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnlzFile {
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
	
	public AnlzFile(String filename) {
		file = new File(filename);
	}
	
	public AnlzFile(String filename, String filename1) {
		file = new File(filename);
		file1 = new File(filename1);
	}
	
	public void defineFile(String fileName) {
		file = new File(fileName);
	}
	
	public void userSearch() {
		
		try {
			String line = null;
			String line1 = null;
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			if (file1 != null) {
				fr1 = new FileReader(file1);
				br1 = new BufferedReader(fr1);
			}
			
			boolean stay = true;
			String holdLine = null;
			boolean stay1 = true;
			int holdLine1 = 0;
			do {
				System.out.println("Would you like to search for a predefined pattern? (Profanity) (Type y or n)");
				holdLine = scanner.nextLine().toLowerCase();
				switch(holdLine) {
				
				
				case "y":
					System.out.println("What would you like to search for? (Please put the number before the item you would like to search for)");
					do {
						System.out.println("[1] Profanity");
						holdLine1 = scanner.nextInt();
						switch(holdLine1) {
						
						
						case 1:
							System.out.println("You have chosen to look for profanity in the file.");
							pattern = "(f[eu]*c*k+)|(sh+i*t+)|(cu+n+t+)|\\b-?(ass+)(ed)?-?(hole)?\\b|(di+c+k+)|(tw+a+t+)|\\b(tit)(ies)?(s)?\\b|(b[ie]+t+c+h+)|(he+ll+)(^o)?(hole)?\\b|(god)?(damn)(it)?|(wh+o+r+e)|(ni+g+er)|\\b(c[uo]+c+k+)( *[fs]u*cker)?\\b|-?(piss)-?|(bastard)(o)?";
							stay1 = false;
							stay = false;
							break;
							
							
						default:
							System.out.println("Please display an integer!");
							break;
						}
					}while(stay1);
					break;
					
				case "n":
					System.out.println("Please put the regular expression you would like to search for:");
					pattern = scanner.nextLine();
					stay = false;
					break;
					
					
				default:
					System.out.println("Please put 'n' or 'y'.");
					break;
				}
			}while(stay);
			
			filter = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
			
			
			//CONTINUE BELOW
			do {
				line = br.readLine();
				line1 = br1.readLine();
				storeLines.add(line);
				
				if (br1 != null) {
					storeUsers.add(line1);
				}
				
				matcher = filter.matcher(line);
				wordMatcher = space.matcher(line);
				
				while(wordMatcher.find()) {
					totalWordCount++;
				}
				
				while(matcher.find()) {
					filterWordCount++;
					if (!storeLineNumbers.contains(lineNumber)) {
						storeLineNumbers.add(lineNumber);
					}
				}
				lineNumber++;
			}while(br.ready());
			
			ratioOfSearch = ((double)filterWordCount/(double)totalWordCount)*100;
			
			System.out.printf("Filtered Word Count: %d\nTotal Word Count: %d\nRatio of Search: %.2f%%\n",filterWordCount,totalWordCount,ratioOfSearch);
			//System.out.printf("The ratio of your word to the amount of words in the file %s is %d to %d. This is %d%% of the whole file.",file.toString(),filterWordCount,totalWordCount,ratioOfSearch);
			System.out.println("Would you like to see all of the lines that matched your criteria? (insert 'y' or 'n')");
			stay = true;
			do {
				holdLine = scanner.nextLine();
				switch(holdLine) {
				case "y":
					for(int i = 0; i < storeLineNumbers.size(); i++) {
						if (br1 != null) {
							System.out.println(Integer.toString(i) + ". " + storeUsers.get(storeLineNumbers.get(i)) + ": " + storeLines.get(storeLineNumbers.get(i)));
						}else {
							System.out.println(Integer.toString(i) + ". " + storeLines.get(storeLineNumbers.get(i)));
						}
					}
					stay = false;
					break;
					
				case "n":
					System.out.println("Ok! Thank you for using File Analyzer!");
					stay = false;
					break;
					
				default:
					System.out.println("Please put 'y' or 'n'.");
				}
			}while(stay);
			
		} catch (FileNotFoundException e) {
			System.out.printf("This file (%s) has launched this exception: %s\n",file.toString(),e.toString());
		}catch(IOException e) {
			System.out.printf("There was an input output exception\n");
		}
		
		try {
			br.close();
			br1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("These files did not close:" + file.toString() + " " + file1.toString());
		}
		
	}
}
