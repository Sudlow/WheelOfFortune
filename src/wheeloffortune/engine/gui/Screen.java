package wheeloffortune.engine.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a screen to be displayed (e.g. the title screen). For an example,
 * see {@link scoobydoo.gui.TitleScreen TitleScreen}
 */
public abstract class Screen {

	protected int width;
	protected int height;
	private List<Component> components = Collections.synchronizedList(new ArrayList<Component>());
	private Color backgroundColor = Color.BLACK;

	public final void validate(int width, int height) {
		this.width = width;
		this.height = height;
		synchronized (components) {
			for (Component component : components) {
				component.registerOwningScreen(null);
			}
			components.clear();
		}
		layout();
	}

	/**
	 * Called when the screen is opened
	 */
	public void onScreenOpened() {
	}

	/**
	 * Called when the screen is closed
	 */
	public void onScreenClosed() {
	}

	/**
	 * Use this method to create and register your components to this screen
	 */
	public void layout() {
	}

	/**
	 * Helper method to add a button to this screen. Returns the button created
	 */
	public Button addButton(int x, int y, int width, int height, String text, String buttonId) {
		Button button = new Button(x, y, width, height, text, buttonId);
		addComponent(button);
		return button;
	}

	/**
	 * Helper method to add a label to this screen. Returns the label created
	 */
	public Label addLabel(int x, int y, int width, int height, String text) {
		Label label = new Label(x, y, width, height, text);
		addComponent(label);
		return label;
	}

	/**
	 * Helper method to add a label to this screen. Returns the label created
	 */
	public Label addLabel(int x, int y, int width, int height, String text, Color color) {
		Label label = new Label(x, y, width, height, text, color);
		addComponent(label);
		return label;
	}

	/**
	 * Helper method to add an image to the screen. Returns the ImageDisplay
	 * created
	 */
	public ImageDisplay addImage(int x, int y, int width, int height, Image image) {
		ImageDisplay display = new ImageDisplay(x, y, width, height, image);
		addComponent(display);
		return display;
	}

	/**
	 * Called to register a component to this screen
	 */
	public void addComponent(Component component) {
		components.add(component);
		component.registerOwningScreen(this);
	}

	public void removeComponent(Component component) {
		components.remove(component);
		component.registerOwningScreen(null);
	}

	/**
	 * Returns a list of all components registered to this screen
	 */
	public List<Component> getComponents() {
		return Collections.unmodifiableList(components);
	}

	/**
	 * Sets the background color
	 */
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	/**
	 * Returns the background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Called when the mouse button is pressed on this screen
	 * 
	 * @param x
	 *            - x-position of the cursor
	 * @param y
	 *            - y-position of the cursor
	 * @param button
	 *            - the button on the mouse pressed (e.g. MouseEvent.BUTTON1)
	 */
	public void mousePressed(int x, int y, int button) {
		synchronized (components) {
			for (Component component : new ArrayList<Component>(components)) {
				component.onMousePressed(x, y, button);
			}
		}
	}

	/**
	 * Called when the mouse button is released on this screen
	 * 
	 * @param x
	 *            - x-position of the cursor
	 * @param y
	 *            - y-position of the cursor
	 * @param button
	 *            - the button on the mouse released (e.g. MouseEvent.BUTTON1)
	 */
	public void mouseReleased(int x, int y, int button) {
		synchronized (components) {
			for (Component component : new ArrayList<Component>(components)) {
				component.onMouseReleased(x, y, button);
			}
		}
	}

	/**
	 * Called to draw this screen
	 */
	public void draw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		synchronized (components) {
			for (Component component : components) {
				component.draw(g);
			}
		}
	}

	/**
	 * Called every tick when this screen is displayed
	 */
	public void updateTick() {
		synchronized (components) {
			for (Component component : new ArrayList<Component>(components)) {
				component.updateTick();
			}
		}
	}

	/**
	 * Called when a button is pressed on this screen
	 */
	public void onButtonPressed(String buttonId) {
	}

}