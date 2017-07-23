package nl.ou.dpd.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        assertThat(Util.nullSafeEquals(null, null), is(true));
        assertThat(Util.nullSafeEquals("a", null), is(false));
        assertThat(Util.nullSafeEquals(null, "a"), is(false));
        assertThat(Util.nullSafeEquals("a", new String("a")), is(true));
        assertThat(Util.nullSafeEquals("a", new StringBuilder("a")), is(false));
    }

    @Test
    public void testLastSubstringOf() {
        assertThat(Util.inverseSubstringOf("12345", 3), is("345"));
        assertThat(Util.inverseSubstringOf("12", 2), is("12"));
        assertThat(Util.inverseSubstringOf("12345", 0), is(""));
        assertThat(Util.inverseSubstringOf("", 0), is(""));

        thrown.expect(StringIndexOutOfBoundsException.class);
        Util.inverseSubstringOf("12345", 6);
    }
}
