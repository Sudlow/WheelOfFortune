package wheeloffortune.engine.gui;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import wheeloffortune.engine.input.Gif;

public class GifDisplay extends Component {

	private ImageIcon image;

	public GifDisplay(int x, int y, int width, int height, Gif gif) {
		super(x, y, width, height);
		this.image = gif.createImageIcon();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image.getImage(), getX(), getY(), getWidth(), getHeight(), null);
	}

}
