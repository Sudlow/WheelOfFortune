package wheeloffortune.engine.sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

class ClipSoundPlayer implements ISoundPlayer {

	private Clip clip;
	private LineListener customListener;

	public ClipSoundPlayer(Clip clip) {
		this.clip = clip;
	}

	@Override
	public void play(String name, final Runnable onStopped) {
		if (clip.isRunning())
			clip.stop();
		if (customListener != null) {
			clip.removeLineListener(customListener);
		}
		if (onStopped != null) {
			customListener = new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						onStopped.run();
					}
				}
			};
			clip.addLineListener(customListener);
		} else {
			customListener = null;
		}
		clip.setFramePosition(0);
		clip.start();
	}

	@Override
	public void close() {
		if (clip.isRunning())
			clip.stop();
		clip.drain();
		clip.close();
	}

}