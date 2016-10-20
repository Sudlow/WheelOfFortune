package wheeloffortune.engine.sound;

public class UnableToPlaySoundException extends RuntimeException {

	private static final long serialVersionUID = -5376624463568693669L;

	public UnableToPlaySoundException(String sound, Throwable cause) {
		super(sound, cause);
	}

	public UnableToPlaySoundException(String sound) {
		super(sound);
	}

}