package wheeloffortune.engine.input;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

	private FontLoader() {
	}

	public static String loadFont(String... ttfFiles) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		String fontName = null;
		for (String ttfFile : ttfFiles) {
			Font font;
			try {
				InputStream fontInputStream = FontLoader.class.getResourceAsStream("/font/" + ttfFile + ".ttf");
				if (fontInputStream == null) {
					throw new IOException("The font resource " + ttfFile + " does not exist");
				}
				font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
			} catch (FontFormatException e) {
				System.err.println("The font has invalid format");
				e.printStackTrace();
				return Font.SANS_SERIF;
			} catch (IOException e) {
				System.err.println("Error loading font");
				e.printStackTrace();
				return Font.SANS_SERIF;
			}
			if (fontName == null) {
				fontName = font.getName();
			}

			ge.registerFont(font);
		}

		return fontName;
	}

}
