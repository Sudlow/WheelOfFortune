package wheeloffortune.engine.gui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Component {

	private int x;
	private int y;
	private int width;
	private int height;
	private Screen owningScreen;

	public static final float TEXT_ALIGN_LEFT = 0;
	public static final float TEXT_ALIGN_CENTER = 0.5f;
	public static final float TEXT_ALIGN_RIGHT = 1;
	public static final float TEXT_ALIGN_TOP = 0;
	public static final float TEXT_ALIGN_MIDDLE = 0.5f;
	public static final float TEXT_ALIGN_BOTTOM = 1;

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

	protected static void drawAlignedString(Graphics g, String text, int x, int y, float hAlignment, float vAlignment) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int lineHeight = fontMetrics.getAscent() + fontMetrics.getDescent();
		g.drawString(text, (int) (x - (fontMetrics.stringWidth(text) * hAlignment)),
				(int) (y + fontMetrics.getAscent() - (lineHeight * vAlignment)));
	}

}