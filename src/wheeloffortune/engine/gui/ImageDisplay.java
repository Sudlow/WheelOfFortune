package wheeloffortune.engine.gui;

import java.awt.Graphics;
import java.awt.Image;

public class ImageDisplay extends Component {

	private Image image;

	public ImageDisplay(int x, int y, int width, int height, Image image) {
		super(x, y, width, height);
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
	}

}