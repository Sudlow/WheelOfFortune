package wheeloffortune.engine.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import wheeloffortune.engine.input.Mouse;
import wheeloffortune.game.Game;

public class Button extends Component {

	private static final Color NORMAL_BG_COLOR = Color.WHITE;
	private static final Color NORMAL_OUTLINE_COLOR = Color.BLACK;
	private static final Color NORMAL_TEXT_COLOR = Color.BLACK;
	private static final Color HOVERED_BG_COLOR = new Color(255, 255, 153);
	private static final Color HOVERED_OUTLINE_COLOR = new Color(0, 0, 32);
	private static final Color HOVERED_TEXT_COLOR = new Color(0, 0, 32);
	private static final Color PRESSED_BG_COLOR = new Color(255, 224, 224);
	private static final Color PRESSED_OUTLINE_COLOR = new Color(32, 0, 0);
	private static final Color PRESSED_TEXT_COLOR = new Color(32, 0, 0);

	private String text;
	private String buttonId;

	public Button(int x, int y, int width, int height, String text, String buttonId) {
		super(x, y, width, height);
		this.text = text;
		this.buttonId = buttonId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private boolean isHovered(int x, int y) {
		return x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight();
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		if (button == MouseEvent.BUTTON1 && isHovered(x, y)) {
			getOwningScreen().onButtonPressed(buttonId);
		}
	}

	@Override
	public void draw(Graphics g) {
		Color bgColor;
		Color outlineColor;
		Color textColor;
		Point mouseLocation = Mouse.getMouseLocation();
		if (isHovered((int) mouseLocation.x, (int) mouseLocation.y)) {
			if (Mouse.isButtonDown(MouseEvent.BUTTON1)) {
				bgColor = PRESSED_BG_COLOR;
				outlineColor = PRESSED_OUTLINE_COLOR;
				textColor = PRESSED_TEXT_COLOR;
			} else {
				bgColor = HOVERED_BG_COLOR;
				outlineColor = HOVERED_OUTLINE_COLOR;
				textColor = HOVERED_TEXT_COLOR;
			}
		} else {
			bgColor = NORMAL_BG_COLOR;
			outlineColor = NORMAL_OUTLINE_COLOR;
			textColor = NORMAL_TEXT_COLOR;
		}

		g.setColor(bgColor);
		g.fillRect(getX(), getY(), getWidth(), getHeight());

		g.setColor(outlineColor);
		g.drawRect(getX(), getY(), getWidth(), getHeight());

		g.setColor(textColor);
		g.setFont(Game.NORMAL_FONT);
		drawCenteredString(g, text, getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

}