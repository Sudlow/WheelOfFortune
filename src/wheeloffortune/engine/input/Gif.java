package wheeloffortune.engine.input;

import javax.swing.ImageIcon;

public class Gif {

	static final Gif FAILED_GIF = new Gif(null) {
		@Override
		public ImageIcon createImageIcon() {
			return new ImageIcon(ImgLoader.FAILED_IMAGE);
		}
	};

	private byte[] data;

	public Gif(byte[] data) {
		this.data = data;
	}

	public ImageIcon createImageIcon() {
		return new ImageIcon(data);
	}

}
