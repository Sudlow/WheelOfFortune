package wheeloffortune.engine.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class ImageDisplay extends Component {

	private Image image;
	private float rotation = 0;
	private boolean constantScale = false;

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

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public boolean isConstantScale() {
		return constantScale;
	}

	public void setConstantScale(boolean constantScale) {
		this.constantScale = constantScale;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Remember transformations are applied backwards with AffineTransform,
		// read the transformations from bottom to top
		if (constantScale) {
			drawConstantScale(g2);
		} else {
			drawNoConstantScale(g2);
		}
	}

	private void drawNoConstantScale(Graphics2D g2) {
		// This transformation rotates stuff rotation degress around (0, 0)
		AffineTransform rotation = AffineTransform.getRotateInstance(Math.toRadians(this.rotation));

		Rectangle2D rotatedImageBounds = new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null));
		rotatedImageBounds = rotation.createTransformedShape(rotatedImageBounds).getBounds2D();

		AffineTransform transformation = new AffineTransform();
		// Translate the image to the correct coords
		transformation.translate(getX(), getY());
		// Scale the image so it fits inside the specified space
		transformation.scale(getWidth() / rotatedImageBounds.getWidth(), getHeight() / rotatedImageBounds.getHeight());
		// Translate the image so no part of it goes into negative coords
		transformation.translate(-rotatedImageBounds.getX(), -rotatedImageBounds.getY());
		// Rotate the image around (0, 0)
		transformation.concatenate(rotation);

		g2.drawImage(image, transformation, null);
	}

	private void drawConstantScale(Graphics2D g2) {
		double scaledWidth = (double) getWidth();
		double scaledHeight = (double) getHeight();
		AffineTransform transformation = new AffineTransform();
		// Translate the image to the correct coords
		transformation.translate(getX(), getY());
		// Translate the image back to where it was
		transformation.translate(scaledWidth / 2, scaledHeight / 2);
		// Rotate the image around (0, 0)
		transformation.rotate(Math.toRadians(rotation));
		// Translate the image so that its centre is at (0, 0)
		transformation.translate(-scaledWidth / 2, -scaledHeight / 2);
		// Scale the image so it fits exactly into this component when not
		// rotated
		transformation.scale(scaledWidth / image.getWidth(null), scaledHeight / image.getHeight(null));

		g2.drawImage(image, transformation, null);
	}

}
