import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SpellChecker {

	private ArrayList words;
	
	public SpellChecker() {
		words = new ArrayList();
	} // SpellChecker
	
	public void loadDictionary(String fileName) {
		BufferedReader reader;
		FileReader freader;
		String holder;
		
		try {
			// create a new filereader to open the given input.
			freader = new FileReader(fileName);
			reader = new BufferedReader(freader);
			
			holder = reader.readLine();
			
			// continue until the end of the file.
			while (holder != null) {
				// add the read-in value to the array of words.
				words.add(holder);
				holder = reader.readLine();
			} // while
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: That file cannot be found.");
		} catch (IOException e) {
			System.out.println("ERROR: The file may be locked. It cannot be read from properly.");
		} // catch
	} // loadDictionary
	
	private int findTheMinimum(int a, int b, int c) {
		int minimum = a;
		
		// find the minimum of the three values a, b, and c.
		if (b < minimum) {
			minimum = b;
		} // if
		
		if (c < minimum) {
			minimum = c;
		} // if
		
		return minimum;
	} // findTheMinimum
	
	private int findDifference(String inputWord, String testWord) {
		int n, m;
		char word1Holder, word2Holder;
		int above, left, diagonal, cost;
		
		n = inputWord.length();
		m = testWord.length();
		
		// if one of the words is empty (a length of zero), the levenshtein distance
		// is simply the length of the input word.
		if (n == 0) {
			return m;
		} // if
		
		if (m == 0) {
			return n;
		} // if
		
		// create a new matrix with n columns and m rows.
		int[][] testMatrix = new int[n + 1][m + 1];

		// instantiate the columns with the numbers 0..n
		for (int x = 0; x <= n; x++) {
			testMatrix[x][0] = x;
		} // for
		
		// instatiate the columns with the numbers 0..m
		for (int x = 0; x <= m; x++) {
			testMatrix[0][x] = x;
		} // for

		// go through the entire matrix. get the character at i column of the input word and 
		// j row of the test word.
		for (int i = 1; i <= n; i++) {
			word1Holder = inputWord.charAt (i - 1);
			
			for (int j = 1; j <= m; j++) {
				word2Holder = testWord.charAt (j - 1);
				
				// find the values of the cell directly above and to the left of the current cell.
				// add one to each of these variables.
				above = testMatrix[i - 1][j] + 1;
				left = testMatrix[i][j - 1] + 1;
				
				// if the two characters are equal, their cost is zero. find the value of the diagonal.
				if (word1Holder == word2Holder) {
					diagonal = testMatrix[i - 1][j - 1];
				} else {
					// if they are not, the cost is 1. find the value of the diagonal + 1.
					diagonal = testMatrix[i - 1][j - 1] + 1;
				} // else

				// find the minimum of the three values (above, left, and diagonal.
				testMatrix[i][j] = findTheMinimum(above, left, diagonal);
			} // for
		} // for
		
		// return the value of the cell to the bottom right of the matrix. this is the levenshtein distance.
		return testMatrix[n][m];
	} // findDifference
	
	public String findClosestMatch(String inputWord) {
		int smallestWord = -1, smallestDistance = 100, holder;
		
		// go through each word to find the word with the least
		// levenshtein distance.
		// if the current word in the list has a shorter
		// levenshtein distance than the last minimum, set it to the minimum.
		for (int x = 0; x < words.size(); x++) {
			holder = findDifference(inputWord, words.get(x).toString());
			
			if (holder < smallestDistance) {
				smallestDistance = holder;
				smallestWord = x;
			} // if
		} // for
		
		// if the minimum distance is 0, the word is spelled correctly
		if (smallestDistance == 0) {
			return "SPELLED CORRECTLY";
		} else {
			// else, return the closest matching word.
			return words.get(smallestWord).toString();
		} // else
	} // findClosestMatch
	
	public void addToDictionary(String word) {
		// add a word to the list of words.
		words.add(word);
	} // addToDictionary
	
	public static void main(String args[]) {
		BufferedReader input;
		String wantToContinue = "yes", word, suggestion;
		SpellChecker check = new SpellChecker();
		
		input = new BufferedReader(new InputStreamReader(System.in));
	
		try {
			// ask the user for a list of words to import.
			System.out.print("Please enter a dictionary file. >> ");
			check.loadDictionary(input.readLine());
	
			// as long as the user wants to continue running the program, progress.
			while(wantToContinue.equalsIgnoreCase("yes")) {
				// ask the user for a word to spellcheck.
				System.out.print("Please enter a word. >> ");
				word = input.readLine();
				
				// get the closest suggestion for the word entered.
				suggestion = check.findClosestMatch(word);
				
				// if the word is spelled correctly, tell the user. otherwise, suggest a change.
				if (suggestion.equals("SPELLED CORRECTLY")) {
					System.out.println("That word is correctly spelled.");
				} else {
					// if the user actually meant to enter the correction, that's fine; move on.
					System.out.print("Did you mean " + suggestion + "? YES/NO >> ");
					wantToContinue = input.readLine();
					
					// otherwise, if ask the user if he or she wants to add his or her version of the word to the dictionary.
					if (wantToContinue.equalsIgnoreCase("NO")) {
						System.out.print("Do you want to add this word to the dictionary? YES/NO >> ");
						wantToContinue = input.readLine();
					
						if (wantToContinue.equalsIgnoreCase("YES")) {
							check.addToDictionary(word);
						} // if
					} // if
				} // else
				
				// ask the user if he or she wants to continue.
				System.out.print("Do you want to try another word? YES/NO >> ");
				wantToContinue = input.readLine();
			} // while
		} catch (IOException e) {
			System.out.println("ERROR: Cannot take keyboard input.");
		} // catch
	} // main
} // SpellChecker
