package wheeloffortune.engine.gui;

import java.awt.Color;
import java.awt.Graphics;

import wheeloffortune.game.Game;

public class Label extends Component {

	private static final Color DEFAULT_COLOR = Color.BLACK;

	private String text;
	private Color color;

	public Label(int x, int y, int width, int height, String text) {
		this(x, y, width, height, text, DEFAULT_COLOR);
	}

	public Label(int x, int y, int width, int height, String text, Color color) {
		super(x, y, width, height);
		this.text = text;
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.setFont(Game.NORMAL_FONT);
		drawCenteredString(g, text, getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

}