package nl.ou.dpd.util;

/**
 * A general utility class.
 *
 * @author Martin de Boer
 */
public final class Util {

    /**
     * Private constructor because this is a utility class that cannot be instantiated
     */
    private Util() {
    }

    /**
     * Performs an equals on two objects in a null-safe manner.
     *
     * @param a the first object
     * @param b the second object
     * @return {@code true} if both objects are equal (or both are null), {@code false} otherwise.
     */
    public static boolean nullSafeEquals(final Object a, final Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    /**
     * Returns the last i characters of a String s.
     *
     * @param s the input string
     * @param i the number of characters to return
     * @return the last i characters of s
     */
    public static String inverseSubstringOf(String s, int i) {
        return s.substring(s.length() - i);
    }
}
