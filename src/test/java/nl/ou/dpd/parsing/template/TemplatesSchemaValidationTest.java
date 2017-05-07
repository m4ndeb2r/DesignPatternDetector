package nl.ou.dpd.parsing.template;

import nl.ou.dpd.parsing.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.net.URL;

import static org.hamcrest.core.Is.is;

/**
 * Tests the schema validation by the {@link TemplatesParserWithConditions} class.
 *
 * @author Martin de Boer
 */
public class TemplatesSchemaValidationTest {

    // The XML Schema for validation
    private static final String XML_SCHEMA = "/templates.xsd";
    // A test file containing invalid XML.
    private static final String INVALID_XML = "/invalid.xml";
    // A test file containing an invalid edge tag.
    private static final String INVALID_TAG_XML = "/template_xsd_test_invalidTag.xml";

    private URL xsdURL;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initXmlSchemaURL() {
        xsdURL = TemplatesSchemaValidationTest.class.getResource(XML_SCHEMA);
    }

    /**
     * Tests the exception handling in case of an invalid tag in the XML.
     */
    @Test
    public void testInvalidTagException() {
        final String xmlPath = getPath(INVALID_TAG_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage(String.format("The pattern template file '%s' could not be parsed.", xmlPath));

        templatesParser.parse(xmlPath, xsdURL);
    }

    /**
     * Tests the exception handling in case of a file that does not contain XML.
     */
    @Test
    public void testInvalidXMLException() {
        final String xmlPath = getPath(INVALID_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage(String.format("The pattern template file '%s' could not be parsed.", xmlPath));

        templatesParser.parse(xmlPath, xsdURL);
    }

	private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
	}
}
