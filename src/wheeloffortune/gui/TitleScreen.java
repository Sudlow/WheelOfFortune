package wheeloffortune.gui;

import java.awt.Color;
import java.awt.Graphics;

import wheeloffortune.engine.gui.Screen;

public class TitleScreen extends Screen {

	public TitleScreen() {
		// Here's a good place to set the background color (default is black)
		setBackgroundColor(new Color(64, 0, 0));
	}

	@Override
	public void layout() {
		// Add components to layout the screen
		// Args are generally x, y, width, height, extra...
		addLabel(width / 2, height / 2, 2, 2, "Text in the middle of the screen", Color.GREEN);
		addButton(width - 150, 0, 150, 100, "Top-right", "topRightButton");
		addButton(0, height - 100, 150, 100, "Bottom-left", "bottomLeftButton");
		addImage(0, 0, 100, 100, Images.test);
	}

	@Override
	public void draw(Graphics g) {
		// Don't forget to do this first so other things are drawn
		super.draw(g);

		// Draw custom things here
		g.setColor(Color.RED);
		g.fillRect(width - 40, height - 40, 40, 40);
	}

	@Override
	public void onButtonPressed(String buttonId) {
		// This is where you handle your button presses
		if ("topRightButton".equals(buttonId)) {
			System.out.println("Top-right button pressed");
		} else if ("bottomLeftButton".equals(buttonId)) {
			System.out.println("Bottom-left button pressed");
		}
	}

}
