package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.rule.Condition;
import nl.ou.dpd.parsing.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link TemplatesParserWithConditions} class.
 *
 * TODO: This is a copy of the TemplatesParserTest. Eventually there must be just one of both.
 * TODO: The TemplatesParserWithConditions will eventually also replace the TemplatesParser.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class TemplatesParserWithConditionsTest {

    // The XML Schema for validation
    private static final String XML_SCHEMA = "/templates.xsd";
    // A test file containing two nodes with the same name.
    private static final String DUPLICATE_NODE_XML = "/template_parser_test_duplicateNode.xml";
    // A test file containing two edges with the same name.
    private static final String DUPLICATE_EDGE_XML = "/template_parser_test_duplicateEdge.xml";
    // A test file containing an invalid edge tag.
    private static final String MISSING_EDGE_FOR_ATTRIBUTE_XML = "/template_parser_test_missingEdgeForAttr.xml";
    // A test file containing an invalid applies attribute, referring to a missing edge.
    private static final String MISSING_EDGE_FOR_RULE_XML = "/template_parser_test_missingEdgeForRule.xml";
    // A test file containing valid XML.
    private static final String ADAPTERTEMPLATES_XML = "/template_adapters.xml";

    private URL xsdURL;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initXmlSchemaURL() {
        xsdURL = TemplatesParserWithConditionsTest.class.getResource(XML_SCHEMA);
    }

    /**
     * Tests the happy flow of parsing an XML templates file. This test uses an XML containing the definition of
     * three Adapter patters: two Object Adapter pattern variaties and one Class Adapter Pattern.
     */
    @Test
    public void testParseAdapterTemplates() {
        final TemplatesParserWithConditions parser = new TemplatesParserWithConditions();
        final String xmlPath = getPath(ADAPTERTEMPLATES_XML);

        final List<DesignPattern> parseResult = parser.parse(xmlPath, xsdURL);

        assertThat(parseResult.size(), is(3));
        assertObjectAdapterWithAbstractClass(parseResult.get(0));
        assertObjectAdapterWithInterface(parseResult.get(1));
        assertClassAdapter(parseResult.get(2));
    }

    private void assertObjectAdapterWithAbstractClass(DesignPattern designPattern) {
        assertThat(designPattern.getEdges().size(), is(3));

        // We expect 12 conditions, being: 6 user conditions + 2 system condtions for attributes + 4 system conditions
        // for node types
        assertThat(designPattern.getConditions().size(), is(12));
        assertSystemConditionsCount(designPattern.getConditions(), 6);
        assertUserConditionsCount(designPattern.getConditions(), 6);
        // TODO: more extensive checking here
    }

    private void assertObjectAdapterWithInterface(DesignPattern designPattern) {
        assertThat(designPattern.getEdges().size(), is(3));

        // We expect 11 conditions, being: 5 user conditions + 2 system condtions for attributes + 4 system conditions
        // for node types
        assertThat(designPattern.getConditions().size(), is(11));
        assertSystemConditionsCount(designPattern.getConditions(), 6);
        assertUserConditionsCount(designPattern.getConditions(), 5);

    }

    private void assertClassAdapter(DesignPattern designPattern) {
        assertThat(designPattern.getEdges().size(), is(3));

        // We expect 8 conditions, being: 3 user conditions + 1 system condtion for attributes + 4 system conditions
        // for node types
        assertThat(designPattern.getConditions().size(), is(8));
        assertSystemConditionsCount(designPattern.getConditions(), 5);
        assertUserConditionsCount(designPattern.getConditions(), 3);

    }

    private void assertSystemConditionsCount(List<Condition> conditions, long expectedAmount) {
        assertThat(conditions.stream().filter(c -> c.getId().startsWith("System")).count(), is(expectedAmount));
    }

    private void assertUserConditionsCount(List<Condition> conditions, long expectedAmount) {
        assertThat(conditions.stream().filter(c -> !c.getId().startsWith("System")).count(), is(expectedAmount));
    }

    /**
     * Tests the exception handling in case of a node id that is not unique, resulting in a {@link ParseException}.
     */
    @Test
    public void testDuplicateNodeException() {
        final String xmlPath = getPath(DUPLICATE_NODE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectMessage("The node id 'C' is not unique in this pattern.");

        templatesParser.parse(xmlPath, xsdURL);
    }

    /**
     * Tests the exception handling in case of an edge id which is not unique, resulting in a {@link ParseException}.
     */
    @Test
    public void testDuplicateEdgeException() {
        final String xmlPath = getPath(DUPLICATE_EDGE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectMessage("The edge id 'AB' is not unique in this pattern.");

        templatesParser.parse(xmlPath, xsdURL);
    }

    /**
     * Tests the exception handling in case of a missing edge fot an attribute. When an attribute is present in a class
     * there must also be a relation for this attribute (between the class it's defined in to the class that represents
     * its type.
     */
    @Test
    public void testMissingEdgeForAttributeException() {
        final String xmlPath = getPath(MISSING_EDGE_FOR_ATTRIBUTE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectMessage("An edge between 'A' and 'B' could not be found.");

        templatesParser.parse(xmlPath, xsdURL);
    }

    /**
     * Tests the exception handling in case of a missing edge. When a rule applies to an element, and this element is
     * not present, this must generate an exception.
     */
    @Test
    public void testMissingEdgeForRuleException() {
        final String xmlPath = getPath(MISSING_EDGE_FOR_RULE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectMessage("Rules must apply to existing elements. No element 'CB' was found.");

        templatesParser.parse(xmlPath, xsdURL);
    }

    /**
     * Tests the exception handling in case of a missing xml-file.
     */
    @Test
    public void testFileNotFoundException() {
        final TemplatesParserWithConditions parser = new TemplatesParserWithConditions();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The pattern template file 'missing.xml' could not be parsed.");

        parser.parse("missing.xml", xsdURL);
    }

	private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
	}
}
