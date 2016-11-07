package wheeloffortune.game;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;

import wheeloffortune.engine.game.CustomJFrame;
import wheeloffortune.engine.game.MainWindow;
import wheeloffortune.engine.gui.Screen;
import wheeloffortune.engine.input.FontLoader;
import wheeloffortune.engine.input.Keyboard;
import wheeloffortune.engine.input.Mouse;
import wheeloffortune.engine.sound.SoundManager;
import wheeloffortune.gui.TitleScreen;

public class Game {

	// CONSTANTS
	public static final String GAME_NAME = "Wheel of Fortune";
	public static final String FONT_NAME = FontLoader.loadFont("font_normal", "font_bold", "font_italic",
			"font_bold_italic");
	public static final Font NORMAL_FONT = new Font(FONT_NAME, Font.PLAIN, 16);
	private static final int TICKS_PER_SECOND = 20;
	private static final int MILLIS_PER_TICK = 1000 / TICKS_PER_SECOND;

	// FIELDS
	private static boolean isRunning = false;
	private static CustomJFrame theFrame;
	private static MainWindow theWindow;
	private static Screen openScreen;
	private static GameLogic logic;

	// MAIN METHOD
	public static void main(String[] args) {
		initializeGame();
		gameLoop();
	}

	// LIFECYCLE METHODS
	private static void initializeGame() {
		logic = new GameLogic();

		theWindow = new MainWindow();
		theFrame = new CustomJFrame(theWindow);

		new Runnable() {
			@Override
			public void run() {
				SoundManager.playSound("menutheme", this);
			}
		}.run();

		openScreen(new TitleScreen());
	}

	private static void gameLoop() {
		isRunning = true;
		while (isRunning) {
			long startTimeNanos = System.nanoTime();

			tick();
			redraw();

			long timeToSleepMillis = MILLIS_PER_TICK - ((System.nanoTime() - startTimeNanos) / 1000000);
			if (timeToSleepMillis > 0) {
				try {
					Thread.sleep(timeToSleepMillis);
				} catch (InterruptedException e) {
					// Ignore this exception
				}
			} else if (timeToSleepMillis < 0) {
				// This means we've gone overtime
				System.err.println("Tickrate is struggling! We're behind by " + (-timeToSleepMillis) + "ms");
			}
		}
	}

	public static void stopGame() {
		SoundManager.closeAllSounds();
		theFrame.dispose();
		isRunning = false;
	}

	private static void tick() {
		// Update input first so that things react to it faster
		Keyboard.updateTick();
		Mouse.updateTick();

		// Update screen stuff
		if (openScreen != null) {
			// Mouse input
			for (int button = MouseEvent.BUTTON1; button <= MouseEvent.BUTTON3; button++) {
				Point pointerLocation = null;

				if (Mouse.isButtonPressed(button)) {
					if (pointerLocation == null) {
						pointerLocation = Mouse.getMouseLocation();
					}
					openScreen.mousePressed(pointerLocation.x, pointerLocation.y, button);
				}

				if (Mouse.isButtonReleased(button)) {
					if (pointerLocation == null) {
						pointerLocation = Mouse.getMouseLocation();
					}
					openScreen.mouseReleased(pointerLocation.x, pointerLocation.y, button);
				}
			}

			// Update
			openScreen.updateTick();
		}
	}

	public static void redraw() {
		theWindow.repaint();
	}

	// GETTERS
	public static Screen getOpenScreen() {
		return openScreen;
	}

	public static void openScreen(Screen screen) {
		if (openScreen != null) {
			openScreen.onScreenClosed();
		}

		openScreen = screen;

		screen.onScreenOpened();
		screen.validate(theWindow.getWidth(), theWindow.getHeight());
	}

	public static CustomJFrame getFrame() {
		return theFrame;
	}

	public static MainWindow getWindow() {
		return theWindow;
	}

	public static GameLogic getLogic() {
		return logic;
	}

}
