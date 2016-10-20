package wheeloffortune.engine.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class SoundManager {

	private static boolean isUnix;
	static boolean mplayerExists;
	private static boolean hasWarnedNoMPlayer = false;

	static {
		String os = System.getProperty("os.name").toLowerCase();
		isUnix = os.contains("nix") || os.contains("nux") || os.contains("aix");

		if (isUnix) {
			try {
				Process process = Runtime.getRuntime().exec("which mplayer-nogui");
				mplayerExists = process.waitFor() == 0;
			} catch (IOException e) {
				mplayerExists = false;
			} catch (InterruptedException e) {
				mplayerExists = false;
			}
		}
	}

	private SoundManager() {
	}

	private static final Map<String, ISoundPlayer> sounds = new HashMap<String, ISoundPlayer>();
	private static final Set<String> loadingSounds = Collections.synchronizedSet(new HashSet<String>());

	/**
	 * Plays the sound with the given name in the resources
	 */
	public static void playSound(String name) {
		playSound(name, null);
	}

	/**
	 * Plays the sound with the given name in the resources, calling
	 * <code>onStopped.run()</code> when the sound has finished playing
	 */
	public static void playSound(final String name, final Runnable onStopped) {
		if (!loadingSounds.add(name)) {
			return;
		}
		Thread soundRunThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Try the cache first
				ISoundPlayer sound = sounds.get(name);

				if (sound == null) {
					byte[] soundBytes = null;
					try {
						// Read the sound to a byte array
						InputStream input = new BufferedInputStream(
								SoundManager.class.getResourceAsStream("/sounds/" + name + ".wav"));
						ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
						byte[] buffer = new byte[8192];
						int bytesRead;
						while ((bytesRead = input.read(buffer)) != -1) {
							bytesOut.write(buffer, 0, bytesRead);
						}
						soundBytes = bytesOut.toByteArray();

						// Try the Java audio system first
						AudioInputStream audioInput = AudioSystem
								.getAudioInputStream(new ByteArrayInputStream(soundBytes));
						DataLine.Info info = new DataLine.Info(Clip.class, audioInput.getFormat());
						Clip clip = (Clip) AudioSystem.getLine(info);
						clip.open(audioInput);
						sound = new ClipSoundPlayer(clip);
					} catch (LineUnavailableException e) {
						if (isUnix) {
							// Unix workaround
							if (mplayerExists) {
								assert soundBytes != null : "Should never be null, LineUnavailableException should never be thrown before it is set";
								try {
									sound = new MPlayerSoundPlayer(new ByteArrayInputStream(soundBytes));
								} catch (IOException e1) {
									e1.printStackTrace();
									sound = new DummySoundPlayer();
								}
							} else {
								// Use a dummy sound player, there is no other
								// alternative
								if (!hasWarnedNoMPlayer) {
									JOptionPane.showMessageDialog(null,
											"The program mplayer-nogui is not installed on this computer, unable to play sounds",
											"Warning", JOptionPane.WARNING_MESSAGE);
									hasWarnedNoMPlayer = true;
								}
								sound = new DummySoundPlayer();
							}
						} else {
							sound = new DummySoundPlayer();
						}
					} catch (UnsupportedAudioFileException e) {
						throw new UnableToPlaySoundException(name, e);
					} catch (IOException e) {
						throw new UnableToPlaySoundException(name, e);
					}
					sounds.put(name, sound);
				}

				sound.play(name, onStopped);
				loadingSounds.remove(name);
			}
		});
		soundRunThread.setName("Play sound " + name);
		soundRunThread.start();
	}

	public static void closeAllSounds() {
		for (ISoundPlayer sound : sounds.values()) {
			sound.close();
		}
		sounds.clear();
	}
}