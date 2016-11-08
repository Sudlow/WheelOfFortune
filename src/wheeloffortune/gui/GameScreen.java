package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Font;

import wheeloffortune.engine.gui.Label;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.game.Game;

public class GameScreen extends Screen {

	private static final Font PHRASE_FONT = new Font(Game.FONT_NAME, Font.PLAIN, 32);

	public GameScreen() {
		setBackgroundColor(new Color(178, 238, 255));
	}

	@Override
	public void layout() {
		addLabel(2, 2, "Current player: " + (Game.getLogic().getCurrentPlayer().getIndex() + 1), Color.RED);
		addLabel(2, 25, "Money: S" + Game.getLogic().getCurrentPlayer().getMoney());
		if (Game.getLogic().getMoneyOnSpinner() != -1) {
			addLabel(width - 2, 2, "Money on spinner: S" + Game.getLogic().getMoneyOnSpinner(), Color.RED)
					.setHAlignment(Label.TEXT_ALIGN_RIGHT);
		}
		if (Game.getLogic().isGuessingPhrase()) {
			StringBuilder phrase = new StringBuilder(Game.getLogic().getCurrentPhrase());
			int lastSpaceIndex = -1;
			int startLineIndex = 0;
			for (int i = 0; i < phrase.length(); i++) {
				char charAt = phrase.charAt(i);
				if (charAt == ' ') {
					lastSpaceIndex = i;
				} else if (Game.getLogic().isLetter(charAt) && !Game.getLogic().isLetterGuessed(charAt)) {
					phrase.setCharAt(i, '_');
				}
				if (i - startLineIndex < 80) {
					if (lastSpaceIndex != -1) {
						phrase.setCharAt(lastSpaceIndex, '\n');
						startLineIndex = lastSpaceIndex;
					}
				}
			}
			addLabel(2, 50, phrase.toString(), Color.BLUE).setFont(PHRASE_FONT);
			if (!Game.getLogic().needsSpin()) {
				for (int i = 0; i < 13; i++) {
					addButton(width * i / 13, height - height / 5, width / 13, height / 10,
							String.valueOf((char) ('A' + i)), "button" + ((char) ('A' + i)));
					addButton(width * i / 13, height - height / 10, width / 13, height / 10,
							String.valueOf((char) ('N' + i)), "button" + ((char) ('N' + i)));
				}
			}
		}
		if (Game.getLogic().needsSpin()) {
			addButton(width / 2 - 300 / 2, height / 2, 300, 100, "SPIN", "spinSpinner");
		}
	}

	@Override
	public void onButtonPressed(String buttonId) {
		if ("spinSpinner".equals(buttonId)) {
			Game.openScreen(new SpinnerScreen());
		} else if (buttonId.startsWith("button")) {
			if (!Game.getLogic().isGuessingPhrase()) {
				return;
			}
			if (Game.getLogic().needsSpin()) {
				return;
			}
			char c = buttonId.charAt(6);
			if (Game.getLogic().isLetterGuessed(c)) {
				return;
			}
			int timesLetterAppeared = Game.getLogic().guessLetter(c);
			Game.getLogic().getCurrentPlayer().setMoney(Game.getLogic().getCurrentPlayer().getMoney()
					+ Game.getLogic().getMoneyOnSpinner() * timesLetterAppeared);
			System.out.println("Letter " + c + " appears " + timesLetterAppeared + " times");
			Game.getLogic().setNeedsSpin(true);
			if (timesLetterAppeared == 0) {
				// We failed :(
				Game.getLogic().nextPlayer();
				Game.getLogic().setGuessingPhrase(false);
			} else {
				if (Game.getLogic().hasGuessedPhrase()) {
					Game.getLogic().getCurrentPlayer()
							.setMoney((int) (Game.getLogic().getCurrentPlayer().getMoney() * 1.1f));
					System.out.println("You guessed the phrase, adding 10% to your money");
				}
			}
			Game.openScreen(new GameScreen());
		}
	}

}
