package nl.ou.dpd.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertTrue;
import static nl.ou.dpd.util.Util.inverseSubstringOf;
import static nl.ou.dpd.util.Util.nullSafeEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;

/**
 * Tests the {@link Util} utility class.
 *
 * @author Martin de Boer
 */
public class UtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNullSafeEquals() {
        assertTrue(nullSafeEquals(null, null));
        assertFalse(nullSafeEquals("a", null));
        assertFalse(nullSafeEquals(null, "a"));
        assertTrue(nullSafeEquals("a", new String("a")));
        assertFalse(nullSafeEquals("a", new StringBuilder("a")));
    }

    @Test
    public void testLastSubstringOf() {
        assertThat(inverseSubstringOf("12345", 3), is("345"));
        assertThat(inverseSubstringOf("12", 2), is("12"));
        assertThat(inverseSubstringOf("12345", 0), is(""));
        assertThat(inverseSubstringOf("", 0), is(""));

        thrown.expect(StringIndexOutOfBoundsException.class);
        inverseSubstringOf("12345", 6);
    }
}
