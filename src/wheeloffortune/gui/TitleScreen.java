package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Graphics;

import wheeloffortune.engine.gui.Screen;
import wheeloffortune.game.Game;

public class TitleScreen extends Screen {

	public TitleScreen() {
		// Here's a good place to set the background color (default is black)
		setBackgroundColor(new Color(178, 238, 255));
	}

	@Override
	public void layout() {
		// Add components to layout the screen
		// Args are generally x, y, width, height, extra...
		
		addButton(width / 3, height / 2 + 50, width / 3, 50, "Play", "topRightButton");
		addButton(width / 3, height / 2 + 150, width / 3, 50, "Exit", "bottomLeftButton");
		addImage(width / 3 , height / 50, width / 3, height / 2, Images.logo);
		
	}

	@Override
	public void draw(Graphics g) {
		// Don't forget to do this first so other things are drawn
		super.draw(g);

		// Draw custom things here
		
	}

	@Override
	public void onButtonPressed(String buttonId) {
		// This is where you handle your button presses
		if ("topRightButton".equals(buttonId)) {
		    Game.openScreen(new PlayerSelect());;
		} else if ("bottomLeftButton".equals(buttonId)) {
			Game.stopGame();
		}
	}

}
