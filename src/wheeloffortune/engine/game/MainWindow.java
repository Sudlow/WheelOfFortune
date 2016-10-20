package wheeloffortune.engine.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import wheeloffortune.engine.gui.Screen;
import wheeloffortune.engine.input.Keyboard;
import wheeloffortune.engine.input.Mouse;
import wheeloffortune.game.Game;

/**
 * Custom JPanel which is used as the JFrame's content pane
 */
public class MainWindow extends JPanel {

	private static final long serialVersionUID = -3880026026104218593L;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	public MainWindow() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(Keyboard.instance());
		addMouseListener(Mouse.instance());
	}

	@Override
	public void paintComponent(Graphics g) {
		Screen screen = Game.getOpenScreen();
		if (screen != null) {
			screen.draw(g);
		}
	}

}