package wheeloffortune.engine.gui;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Component {

	private int x;
	private int y;
	private int width;
	private int height;
	private Screen owningScreen;

	public Component(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Screen getOwningScreen() {
		return owningScreen;
	}

	public void registerOwningScreen(Screen screen) {
		this.owningScreen = screen;
	}

	public void onMousePressed(int x, int y, int button) {
	}

	public void onMouseReleased(int x, int y, int button) {
	}

	public void updateTick() {
	}

	public abstract void draw(Graphics g);

	protected static void drawCenteredString(Graphics g, String text, int x, int y) {
		Rectangle2D textSize = g.getFontMetrics().getStringBounds(text, g);
		g.drawString(text, x - (int) (textSize.getWidth() / 2), y + (int) (textSize.getHeight() / 4));
	}

}