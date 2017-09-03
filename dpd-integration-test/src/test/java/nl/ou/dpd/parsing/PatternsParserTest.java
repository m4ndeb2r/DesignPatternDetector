package nl.ou.dpd.parsing;

import nl.ou.dpd.IntegrationTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;

import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link PatternsParser} class for handling file exceptions. We do not need to test the happy flow here,
 * because we have several integration test for matching patterns, that do succesfully parse the XML input as well.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class PatternsParserTest {

    // A test file containing invalid XML.
    private static final String INVALID_XML = "/patterns/invalid.xml";

    private PatternsParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initParser() {
        parser = ParserFactory.createPatternParser();
    }

    /**
     * Test the processing of an invalid XML file.
     */
    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XML);

        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("The design pattern template file could not be parsed.");

        parser.parse(path);
    }
    /**
     * Tests the exception handling in case of a {@link FileNotFoundException} during parsing a patterns file.
     */
    @Test
    public void testFileNotFoundException() {
        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The design pattern template file could not be parsed.");

        parser.parse("missing.xml");
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
