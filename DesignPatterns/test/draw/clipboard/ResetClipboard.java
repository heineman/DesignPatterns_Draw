package draw.clipboard;

/**
 * Enables a Singleton object to be removed.
 * 
 * One problem with Singleton is that in a sequence of JUnit tests, the first one that constructs the Singleton
 * wins, and all others fail. This class shows how to reset, but only when the static instance for the Singleton
 * is package private.
 * 
 * ONLY access this capability during test cases
 * @since draw.4
 */
public class ResetClipboard {
	public static ClipboardManager resetSingleton() {
		ClipboardManager.inst = null;
		return ClipboardManager.getInstance();
	}
}
