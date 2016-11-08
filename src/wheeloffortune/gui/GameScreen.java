package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Font;

import wheeloffortune.engine.gui.Label;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.engine.input.Keyboard;
import wheeloffortune.game.Game;

public class GameScreen extends Screen {

	private static final Font PHRASE_FONT = new Font(Game.FONT_NAME, Font.PLAIN, 32);

	public GameScreen() {
		setBackgroundColor(new Color(178, 238, 255));
	}

	@Override
	public void layout() {
		addLabel(2, 2, "Current player: " + (Game.getLogic().getCurrentPlayer().getIndex() + 1), Color.RED);
		addLabel(width - 2, 2, "Money on spinner: S" + Game.getLogic().getMoneyOnSpinner(), Color.RED)
				.setHAlignment(Label.TEXT_ALIGN_RIGHT);
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
			addLabel(2, 30, phrase.toString(), Color.BLUE).setFont(PHRASE_FONT);
		}
		addLabel(2, height - 2, "Type a letter to guess it").setVAlignment(Label.TEXT_ALIGN_BOTTOM);
	}

	@Override
	public void updateTick() {
		super.updateTick();
		for (char c = 'A'; c <= 'Z'; c++) {
			if (Keyboard.isKeyPressed("letter" + c)) {
				
			}
		}
	}

}
