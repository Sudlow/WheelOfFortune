package wheeloffortune.engine.sound;

interface ISoundPlayer extends AutoCloseable {

	void play(String name, Runnable onStopped);
	
	@Override
	void close();
	
}