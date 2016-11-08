package wheeloffortune.engine.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class controls input from the keyboard
 */
public class Keyboard extends KeyAdapter {

	private static final Keyboard INSTANCE = new Keyboard();

	private final Map<Integer, Set<String>> keyBindingsCodeToName = new HashMap<Integer, Set<String>>();
	private final Map<String, Set<Integer>> keyBindingsNameToCode = new HashMap<String, Set<Integer>>();
	private final Set<Integer> keysPressed = new HashSet<Integer>();
	private final Set<Integer> keysReleased = new HashSet<Integer>();
	private final Set<Integer> keysDown = new HashSet<Integer>();
	private final Set<Integer> pendingKeysPressed = new HashSet<Integer>();
	// This has to be more complicated than the mouse due to repeat key events,
	// especially on Linux
	private final Map<Integer, Integer> ticksSinceKeyReleased = new HashMap<Integer, Integer>();
	private boolean enableRepeatEvents = false;

	private static final int TICKS_FOR_KEY_RELEASE = 1;

	private final Object SYNC_LOCK = new Object();

	private Keyboard() {
	}

	public static Keyboard instance() {
		return INSTANCE;
	}

	public static void registerKeyBindings() {
		for (int i = 0; i < 26; i++) {
			bindKey(KeyEvent.VK_A + i, "letter" + ('A' + i));
		}
	}

	/**
	 * This is how to add a key binding
	 * 
	 * @param keyCode
	 *            - E.g. KeyEvent.VK_SPACE
	 * @param name
	 *            - the name of the key binding
	 */
	public static void bindKey(int keyCode, String name) {
		INSTANCE.doBindKey(keyCode, name);
	}

	private void doBindKey(int keyCode, String name) {
		if (!keyBindingsCodeToName.containsKey(keyCode)) {
			keyBindingsCodeToName.put(keyCode, new HashSet<String>());
		}
		keyBindingsCodeToName.get(keyCode).add(name);

		if (!keyBindingsNameToCode.containsKey(name)) {
			keyBindingsNameToCode.put(name, new HashSet<Integer>());
		}
		keyBindingsNameToCode.get(name).add(keyCode);
	}

	/**
	 * Returns true if the given key binding was pressed between this tick and
	 * the last
	 * 
	 * @param keyBinding
	 * @return
	 */
	public static boolean isKeyPressed(String keyBinding) {
		return INSTANCE.doIsKeyPressed(keyBinding);
	}

	private boolean doIsKeyPressed(String keyBinding) {
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if (possibleKeys == null) {
			return false;
		}
		synchronized (SYNC_LOCK) {
			for (Integer keyPressed : keysPressed) {
				if (possibleKeys.contains(keyPressed)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the given key binding was released between this tick and
	 * the last
	 * 
	 * @param keyBinding
	 * @return
	 */
	public static boolean isKeyReleased(String keyBinding) {
		return INSTANCE.doIsKeyReleased(keyBinding);
	}

	private boolean doIsKeyReleased(String keyBinding) {
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if (possibleKeys == null) {
			return false;
		}
		synchronized (SYNC_LOCK) {
			for (Integer keyReleased : keysReleased) {
				if (possibleKeys.contains(keyReleased)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the given key binding is held down
	 * 
	 * @param keyBinding
	 * @return
	 */
	public static boolean isKeyDown(String keyBinding) {
		return INSTANCE.doIsKeyDown(keyBinding);
	}

	private boolean doIsKeyDown(String keyBinding) {
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if (possibleKeys == null) {
			return false;
		}
		synchronized (SYNC_LOCK) {
			for (Integer keyDown : keysDown) {
				if (possibleKeys.contains(keyDown)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Virtually press the given key
	 * 
	 * @param keyCode
	 *            - E.g. KeyEvent.VK_SPACE
	 */
	public static void pressKey(int keyCode) {
		INSTANCE.doPressKey(keyCode);
	}

	private void doPressKey(int keyCode) {
		synchronized (SYNC_LOCK) {
			if (enableRepeatEvents || !keysDown.contains(keyCode))
				pendingKeysPressed.add(keyCode);
		}
	}

	/**
	 * Virtually press the given key binding
	 * 
	 * @param keyBinding
	 */
	public static void pressKey(String keyBinding) {
		INSTANCE.doPressKey(keyBinding);
	}

	private void doPressKey(String keyBinding) {
		if (keyBindingsNameToCode.containsKey(keyBinding)) {
			for (Integer key : keyBindingsNameToCode.get(keyBinding)) {
				doPressKey(key);
			}
		}
	}

	/**
	 * Virtually release the given key
	 * 
	 * @param keyCode
	 *            - E.g. KeyEvent.VK_SPACE
	 */
	public static void releaseKey(int keyCode) {
		INSTANCE.doReleaseKey(keyCode);
	}

	private void doReleaseKey(int keyCode) {
		synchronized (SYNC_LOCK) {
			ticksSinceKeyReleased.put(keyCode, 0);
		}
	}

	/**
	 * Virtually release the given key binding
	 * 
	 * @param keyBinding
	 */
	public static void releaseKey(String keyBinding) {
		INSTANCE.doReleaseKey(keyBinding);
	}

	private void doReleaseKey(String keyBinding) {
		if (keyBindingsNameToCode.containsKey(keyBinding)) {
			for (Integer key : keyBindingsNameToCode.get(keyBinding)) {
				doReleaseKey(key);
			}
		}
	}

	public static void clearKeys() {
		INSTANCE.doClearKeys();
	}

	private void doClearKeys() {
		synchronized (SYNC_LOCK) {
			for (Integer keyDown : keysDown) {
				ticksSinceKeyReleased.put(keyDown, TICKS_FOR_KEY_RELEASE);
			}
		}
	}

	/**
	 * Enable the key press event to be fired repeatedly while the key is held
	 * down
	 */
	public static void enableRepeatEvents() {
		INSTANCE.doEnableRepeatEvents();
	}

	private void doEnableRepeatEvents() {
		synchronized (SYNC_LOCK) {
			enableRepeatEvents = true;
		}
	}

	public static void disableRepeatEvents() {
		INSTANCE.doDisableRepeatEvents();
	}

	private void doDisableRepeatEvents() {
		synchronized (SYNC_LOCK) {
			enableRepeatEvents = false;
		}
	}

	/**
	 * Must be called every tick to update the key press and release logic
	 */
	public static void updateTick() {
		INSTANCE.doUpdateTick();
	}

	private void doUpdateTick() {
		synchronized (SYNC_LOCK) {
			keysPressed.clear();
			for (Integer key : pendingKeysPressed) {
				keysPressed.add(key);
				keysDown.add(key);
			}
			pendingKeysPressed.clear();

			keysReleased.clear();
			Map<Integer, Integer> ticksSinceKeyReleasedCopy = new HashMap<Integer, Integer>(ticksSinceKeyReleased);
			for (Map.Entry<Integer, Integer> time : ticksSinceKeyReleasedCopy.entrySet()) {
				if (enableRepeatEvents || time.getValue() >= TICKS_FOR_KEY_RELEASE) {
					keysReleased.add(time.getKey());
					keysDown.remove(time.getKey());
					ticksSinceKeyReleased.remove(time.getKey());
				} else {
					ticksSinceKeyReleased.put(time.getKey(), time.getValue() + 1);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		doPressKey(event.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent event) {
		doReleaseKey(event.getKeyCode());
	}

	static {
		registerKeyBindings();
	}
}