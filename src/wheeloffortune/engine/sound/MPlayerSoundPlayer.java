package wheeloffortune.engine.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

class MPlayerSoundPlayer implements ISoundPlayer {

	private static int nextId = 0;

	private File sound;
	private Process process;

	public MPlayerSoundPlayer(InputStream sound) throws IOException {
		assert SoundManager.mplayerExists;
		this.sound = File.createTempFile("stepfish_sound_" + (nextId++), "wav");
		this.sound.deleteOnExit();
		Files.copy(sound, this.sound.toPath());
	}

	@Override
	public void play(final String name, final Runnable onStopped) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (process != null) {
						process = Runtime.getRuntime().exec("mplayer-nogui \"" + sound.getAbsolutePath() + "\"");
						process.waitFor();
						synchronized (MPlayerSoundPlayer.this) {
							onStopped.run();
							process = null;
						}
					}
				} catch (IOException e) {
					throw new UnableToPlaySoundException(name, e);
				} catch (InterruptedException e) {
					throw new UnableToPlaySoundException(name, e);
				}
			}
		});
		thread.start();
	}

	@Override
	public void close() {
		synchronized (this) {
			if (process != null) {
				process.destroy();
			}
		}
	}

}