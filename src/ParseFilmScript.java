import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParseFilmScript {
	public static final String INPUT_FILE_1 = "ep1.txt";
	public static final String INPUT_FILE_2 = "ep2.txt";
	public static final String INPUT_FILE_3 = "ep3.txt";
	public static final String INPUT_FILE_4 = "ep4.txt";
	public static final String INPUT_FILE_5 = "ep5.txt";
	public static final String INPUT_FILE_6 = "ep6.txt";
	public static final String INPUT_FILE_7 = "ep7.txt";

	// debug
	public static final String OUTPUT_FILE = "testing.txt";
	public static final int EXTRA_TABS = 1; // for displaying the character list

	// associate characters with lines of dialogue
	public static Map<String, ArrayList<String>> characterDialogueMap = new HashMap<>();

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		File ep4 = new File(INPUT_FILE_4);
		boolean keepPrinting = true;

		try {
			String line;
			FileReader fileReader = new FileReader(ep4);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			FileWriter fileWriter = new FileWriter(OUTPUT_FILE, false);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// create Pattern objects from regular expressions
			Pattern twentySpaces = Pattern.compile(" {20}[A-Z]*");
			Pattern tenSpaces = Pattern.compile("^\\s{10}\\S.*");

			// declare Matcher instances from the Pattern instances
			Matcher nameMatcher;
			Matcher dialogueMatcher;

			ArrayList temp;
			while ((line = bufferedReader.readLine()) != null) {
				temp = new ArrayList<String>();
				nameMatcher = twentySpaces.matcher(line);
				if(nameMatcher.matches()) {
					String name = line.trim();

					if(!characterDialogueMap.containsKey(name)) {
						characterDialogueMap.put(name, new ArrayList<>());
					}

					line = bufferedReader.readLine();
					dialogueMatcher = tenSpaces.matcher(line);
					while(dialogueMatcher.matches()) {
						String dialogue = line.trim();
						temp = characterDialogueMap.get(name);
						temp.add(dialogue);
						characterDialogueMap.put(name, temp);

						line = bufferedReader.readLine();
						dialogueMatcher = tenSpaces.matcher(line);
					}
				}
			}

			System.out.println("\nFind the dialogue of a character in \"A New Hope\".\n");

			while (keepPrinting) {
				System.out.println("Characters: ");
				int count = 0;
				int longestLength = 0;

				// find longest character name
				for(String character : characterDialogueMap.keySet()) {
					if(character.length() > longestLength) {
						longestLength = character.length();
					}
				}

				// get space required for longest name, for formatting
				int maxTabs = (longestLength + (longestLength % 4) + 4 * EXTRA_TABS) / 4;
				for(String s : characterDialogueMap.keySet()) {
					System.out.print(s);

					// numTabs = maxTabs - integer divide length of longest name by 4
					// lengths 0-3 get rounded down to 0 (the amount to subtract from maxTabs)
					// lengths 4-7 get rounded down to 1
					// lengths 8-11 get rounded down to 2, etc
					int numTabs = maxTabs - s.length() / 4;
					for(int i = 0; i < numTabs; i++) {
						System.out.print("\t");
					}
					count++;
					if(count % 4 == 0)
						System.out.println();
				}

				System.out.print("\n\n> ");
				try {
					prettyPrint(keyboard.next());
				} catch (Exception e) {
					System.out.println("Character not found.");
				}

				System.out.print("Again? y/n ");
				if(keyboard.next().equals("n")) {
					keepPrinting = false;
				}
				System.out.println();
			}
			bufferedReader.close();
			bufferedWriter.close();

		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found. ");
		} catch (IOException e) {
			System.out.println("Error: File read error. ");
		}
	}

	public static void prettyPrint(String character) {
		String dialogue = "";
		for(String s : characterDialogueMap.get(character)) {
			dialogue += s + " ";
		}

		ArrayList<Character> dialogueCharArray = new ArrayList<>();
		for(char c : dialogue.toCharArray()) {
			dialogueCharArray.add(c);
		}

		// store each sentence of a character's dialogue
		char[] sentence = new char[dialogueCharArray.size()];
		int sentenceCount = 0;

		// traverse charArray representation of dialogue
		for(int i = 0; i < dialogueCharArray.size(); i++) {
			char current = dialogueCharArray.get(i);

			// current character ends the sentence
			if(current == '.' || current == '?' || current == '!') {
				sentence[i] = current;
				while(dialogueCharArray.get(i).equals('.') || dialogueCharArray.get(i).equals('?') || dialogueCharArray.get(i).equals('?')) {
					sentence[i] = dialogueCharArray.get(i);
					i++;
				}

				// print and reset the sentence holder
				sentenceCount++;
				System.out.println("   " + sentenceCount + ". " + new String(sentence).trim());
				sentence = new char[dialogueCharArray.size()];

			} else {
				sentence[i] = current;
			}
		}
	}
}
