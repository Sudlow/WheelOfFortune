package wheeloffortune.engine.sound;

class DummySoundPlayer implements ISoundPlayer {

	@Override
	public void play(String name, Runnable onStopped) {
		// nop
	}

	@Override
	public void close() {
		// nop
	}

}