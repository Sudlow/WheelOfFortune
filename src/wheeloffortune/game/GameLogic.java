package wheeloffortune.game;

import java.util.Arrays;

import wheeloffortune.game.SpinnerAction.Bankrupt;
import wheeloffortune.game.SpinnerAction.MissTurn;
import wheeloffortune.game.SpinnerAction.Money;

public class GameLogic {

	private int moneyOnSpinner;
	private String currentPhrase;
	private boolean[] guessedLetters = new boolean[26];
	private Player[] players;
	private int currentPlayerTurn;
	private int currentSpinnerSpeed;
	private float currentSpinnerAngle;

	private static final SpinnerAction[] SPINNER_ACTIONS = {
			new Money(250), // top
			new Money(300),
			new Bankrupt(),
			new Money(750),
			new Money(250),
			new Money(300),
			new Money(200), // right
			new Money(2500),
			new Money(500),
			new Money(400),
			new Money(300),
			new Money(200),
			new Bankrupt(), // bottom
			new Money(5000),
			new Money(200),
			new Money(500),
			new Money(450),
			new MissTurn(),
			new Money(400), // left
			new Money(250),
			new Money(900),
			new Money(150),
			new Money(400),
			new Money(600)
	};

	public int getMoneyOnSpinner() {
		return moneyOnSpinner;
	}

	public void setMoneyOnSpinner(int moneyOnSpinner) {
		this.moneyOnSpinner = moneyOnSpinner;
	}

	public String getCurrentPhrase() {
		return currentPhrase;
	}

	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}
	
	public boolean isLetter(char letter) {
		letter = Character.toUpperCase(letter);
		return letter >= 'A' || letter <= 'Z';
	}
	
	public boolean isLetterGuessed(char letter) {
		letter = Character.toUpperCase(letter);
		if (letter < 'A' || letter > 'Z') {
			throw new IllegalArgumentException("\"" + letter + "\" is not a letter!");
		}
		return guessedLetters[letter - 'A'];
	}
	
	/**
	 * Returns true if the phrase contained the letter
	 */
	public boolean guessLetter(char letter) {
		letter = Character.toUpperCase(letter);
		if (letter < 'A' || letter > 'Z') {
			throw new IllegalArgumentException("\"" + letter + "\" is not a letter!");
		}
		guessedLetters[letter - 'A'] = true;
		return currentPhrase.contains(String.valueOf(letter));
	}
	
	public void newGame(int numberOfPlayers) {
		moneyOnSpinner = -1;
		currentPhrase = "";
		Arrays.fill(guessedLetters, false);
		players = new Player[numberOfPlayers];
		currentPlayerTurn = 0;
		currentSpinnerSpeed = 0;
		currentSpinnerAngle = 0;
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

	public int getCurrentSpinnerSpeed() {
		return currentSpinnerSpeed;
	}

	public void setCurrentSpinnerSpeed(int currentSpinnerSpeed) {
		this.currentSpinnerSpeed = currentSpinnerSpeed;
	}

	public float getCurrentSpinnerAngle() {
		return currentSpinnerAngle;
	}

	public void setCurrentSpinnerAngle(float currentSpinnerAngle) {
		this.currentSpinnerAngle = currentSpinnerAngle;
	}
	
	/**
	 * Returns whether the player should continue their turn
	 */
	public boolean performCurrentSpinnerAction() {
		// First add this value because the spinner image is aligned to the middle of the slot
		float spinnerAngle = currentSpinnerAngle + (24f / 360f / 2f);
		// Wrap the angle to 360
		spinnerAngle %= 360;
		// Convert spinner angle to slot
		int currentSlot = (int) spinnerAngle * 24 / 360;
		// Perform the action!!! @_@
		return SPINNER_ACTIONS[currentSlot].performAction();
	}

}