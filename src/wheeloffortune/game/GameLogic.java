package wheeloffortune.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import wheeloffortune.game.SpinnerAction.Bankrupt;
import wheeloffortune.game.SpinnerAction.MissTurn;
import wheeloffortune.game.SpinnerAction.Money;

public class GameLogic {

	private int moneyOnSpinner;
	private boolean guessingPhrase;
	private String currentPhrase;
	private boolean[] guessedLetters = new boolean[26];
	private Player[] players;
	private int currentPlayerTurn;
	private float currentSpinnerAngle;
	private List<String> phrases;
	private boolean needsSpin;

	private static final SpinnerAction[] SPINNER_ACTIONS = { new Money(250), // top
			new Money(300), new Bankrupt(), new Money(750), new Money(250), new Money(300), new Money(200), // right
			new Money(2500), new Money(500), new Money(400), new Money(300), new Money(200), new Bankrupt(), // bottom
			new Money(5000), new Money(200), new Money(500), new Money(450), new MissTurn(), new Money(400), // left
			new Money(250), new Money(900), new Money(150), new Money(400), new Money(600) };

	public GameLogic() {
		importtextfile();
	}

	public int getMoneyOnSpinner() {
		return moneyOnSpinner;
	}

	public void setMoneyOnSpinner(int moneyOnSpinner) {
		this.moneyOnSpinner = moneyOnSpinner;
	}

	public boolean hasGuessedPhrase() {
		for (int i = 0; i < currentPhrase.length(); i++) {
			char charAt = currentPhrase.charAt(i);
			if (isLetter(charAt) && !isLetterGuessed(charAt)) {
				return false;
			}
		}
		return true;
	}

	public boolean isGuessingPhrase() {
		return guessingPhrase;
	}

	public void chooseNewPhrase() {
		currentPhrase = phrases.get((int) (Math.random() * phrases.size()));
		Arrays.fill(guessedLetters, false);
	}

	public void setGuessingPhrase(boolean guessingPhrase) {
		this.guessingPhrase = guessingPhrase;
	}

	public String getCurrentPhrase() {
		return currentPhrase;
	}

	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}

	public boolean isLetter(char letter) {
		letter = Character.toUpperCase(letter);
		return letter >= 'A' && letter <= 'Z';
	}

	public boolean isLetterGuessed(char letter) {
		letter = Character.toUpperCase(letter);
		if (letter < 'A' || letter > 'Z') {
			throw new IllegalArgumentException("\"" + letter + "\" is not a letter!");
		}
		return guessedLetters[letter - 'A'];
	}

	/** Returns the number of times the letter appeared */
	public int guessLetter(char letter) {
		letter = Character.toUpperCase(letter);
		if (letter < 'A' || letter > 'Z') {
			throw new IllegalArgumentException("\"" + letter + "\" is not a letter!");
		}
		guessedLetters[letter - 'A'] = true;
		int count = 0;
		for (int i = 0; i < currentPhrase.length(); i++) {
			if (Character.toUpperCase(currentPhrase.charAt(i)) == letter) {
				count++;
			}
		}
		return count;
	}

	public void newGame(int numberOfPlayers) {
		moneyOnSpinner = -1;
		players = new Player[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			players[i] = new Player(i);
		}
		currentPlayerTurn = 0;
		currentSpinnerAngle = 0;
		needsSpin = true;
		chooseNewPhrase();

		System.out.println("Started new game with " + numberOfPlayers + " players");
	}

	public Player getCurrentPlayer() {
		return players[currentPlayerTurn];
	}

	public void nextPlayer() {
		currentPlayerTurn++;
		if (currentPlayerTurn == players.length) {
			currentPlayerTurn = 0;
		}
	}

	public float getCurrentSpinnerAngle() {
		return currentSpinnerAngle;
	}

	public void setCurrentSpinnerAngle(float currentSpinnerAngle) {
		this.currentSpinnerAngle = currentSpinnerAngle;
	}

	public SpinnerAction getCurrentSpinnerAction() {
		// First add this value because the spinner image is aligned to the
		// middle of the slot
		float spinnerAngle = currentSpinnerAngle + (360f / 24f / 2f);
		// Wrap the angle to 360
		while (spinnerAngle < 0) {
			spinnerAngle += 360;
		}
		spinnerAngle %= 360;
		// Convert spinner angle to slot
		int currentSlot = (int) spinnerAngle * 24 / 360;
		// At the moment the current slot is in reverse order
		currentSlot = (24 - currentSlot) % 24;

		return SPINNER_ACTIONS[currentSlot];
	}

	public boolean needsSpin() {
		return needsSpin;
	}

	public void setNeedsSpin(boolean needsSpin) {
		this.needsSpin = needsSpin;
	}

	public void importtextfile() {
		try {
			InputStream input = GameLogic.class.getResourceAsStream("/text files/" + "titles" + ".txt");
			if (input == null) {
				throw new IOException("input == null");
			}
			Scanner trump = new Scanner(input);
			phrases = new ArrayList<String>();
			while (trump.hasNextLine()) {

				phrases.add(trump.nextLine());
			}
			trump.close();

		} catch (IOException e) {
			System.err.println("Failed to load text file.");
			throw new IllegalStateException();
		}
	}

}
