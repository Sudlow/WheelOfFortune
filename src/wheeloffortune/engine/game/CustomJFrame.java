package wheeloffortune.engine.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import wheeloffortune.engine.input.Keyboard;
import wheeloffortune.engine.input.Mouse;
import wheeloffortune.game.Game;

/**
 * Custom JFrame class
 */
public class CustomJFrame extends JFrame {

	private static final long serialVersionUID = 5862168449191459872L;

	public CustomJFrame(MainWindow window) {
		super(Game.GAME_NAME);

		setContentPane(window);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// This runs when the user presses the 'x' button
				Game.stopGame();
			}
		});
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				Keyboard.clearKeys();
				Mouse.clearButtons();
			}
		});
		requestFocusInWindow();
		// setResizable(false);
		setVisible(true);
	}

	@Override
	public void validate() {
		super.validate();
		if (Game.getOpenScreen() != null) {
			Game.getOpenScreen().validate(Game.getWindow().getWidth(), Game.getWindow().getHeight());
		}
	}

}