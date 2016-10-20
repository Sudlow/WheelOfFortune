package wheeloffortune.engine.input;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * This class is used to load images
 */
public class ImgLoader {

	private ImgLoader() {
	}

	private static final Image FAILED_IMAGE = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

	static {
		Graphics g = FAILED_IMAGE.getGraphics();
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, 8, 8);
		g.fillRect(8, 8, 8, 8);
		g.setColor(Color.BLACK);
		g.fillRect(8, 0, 8, 8);
		g.fillRect(0, 8, 8, 8);
		g.dispose();
	}

	/**
	 * Loads an image
	 * 
	 * @param resKey
	 *            - the name of the image in the resources
	 */
	public static Image loadImage(String resKey) {
		try {
			InputStream input = ImgLoader.class.getResourceAsStream("/images/" + resKey + ".png");
			if (input == null) {
				throw new IOException("input == null");
			}
			return ImageIO.read(input);
		} catch (IOException e) {
			System.err.println("Failed to load image with resKey " + resKey);
			return FAILED_IMAGE;
		}
	}
}