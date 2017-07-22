package nl.ou.dpd.parsing;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A helper class for unit testing.
 *
 * @author Martin de Boer
 */
public class ParseTestHelper {

    /**
     * Private constructor, because this is a utility class.
     */
    private ParseTestHelper() {
    }

    /**
     * Creates a representation of an attribute of an XML tag, with a name and a value.
     *
     * @param name  the name of the attribute
     * @param value the value of the attribute
     * @return the created {@link Attribute}
     */
    protected static Attribute makeAttributeMock(String name, String value) {
        final Attribute mock = mock(Attribute.class);
        when(mock.getName()).thenReturn(QName.valueOf(name));
        when(mock.getValue()).thenReturn(value);
        return mock;
    }
}
