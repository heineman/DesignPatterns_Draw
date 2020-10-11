package draw.tools;

/**
 * Enables a Singleton object to be removed.
 * 
 * One problem with Singleton is that in a sequence of JUnit tests, the first one that constructs the Singleton
 * wins, and all others fail. This class shows how to reset, but only when the static instance for the Singleton
 * is package private.
 * 
 * ONLY access this capability during test cases
 */
public class ResetTools {
	public static Tools resetSingleton() {
		Tools.instance = null;
		return Tools.getInstance();
	}
}
