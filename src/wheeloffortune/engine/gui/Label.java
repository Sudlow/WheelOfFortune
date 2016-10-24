package wheeloffortune.engine.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import wheeloffortune.game.Game;

public class Label extends Component {

	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final Font FONT = Game.NORMAL_FONT;

	private String text;
	private Color color;
	private float hAlignment = TEXT_ALIGN_LEFT;
	private float vAlignment = TEXT_ALIGN_TOP;

	public Label(int x, int y, String text) {
		this(x, y, text, DEFAULT_COLOR);
	}

	public Label(int x, int y, String text, Color color) {
		super(x, y, 0, 0);
		this.text = text;
		this.color = color;
	}

	@Override
	public int getWidth() {
		Graphics g = Game.getWindow().getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics(FONT);
		return fontMetrics.stringWidth(text);
	}

	@Override
	public void setWidth(int width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getHeight() {
		Graphics g = Game.getWindow().getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics(FONT);
		return fontMetrics.getAscent() + fontMetrics.getDescent();
	}

	@Override
	public void setHeight(int height) {
		throw new UnsupportedOperationException();
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

	public float getHAlignment() {
		return hAlignment;
	}

	public void setHAlignment(float hAlignment) {
		this.hAlignment = hAlignment;
	}

	public float getVAlignment() {
		return vAlignment;
	}

	public void setVAlignment(float vAlignment) {
		this.vAlignment = vAlignment;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.setFont(FONT);
		drawAlignedString(g, text, getX(), getY(), hAlignment, vAlignment);
	}

}