package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Parameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ATTRIBUTE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.CLASS;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.DATATYPE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.HREF;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ID;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.INTERFACE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.IS_ABSTRACT;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.KIND;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.MODEL;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.OPERATION;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.PARAMETER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ArgoUMLRelationParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLRelationParserTest {

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmiFile = getPath("/argoUML/dummy.xmi");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    XMLEvent modelEvent;

    private Map<String, Node> nodes;

    private ArgoUMLRelationParser relationParser;

    /**
     * Initialises the test subject with a mocked xmlInputStream.
     *
     * @throws XMLStreamException not expected.
     */
    @Before
    public void initRelationParser() throws XMLStreamException {
        relationParser = new ArgoUMLRelationParser(xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
    }

    @Before
    public void initNodes() {
        nodes = new HashMap<>();
    }

    /**
     * Mocks the order in which the event reader reads the XML elements.
     *
     * @throws XMLStreamException not expected
     */
    @Before
    public void initEventReader() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // model start-element
                true, // model end-element
                false
        );
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent, modelEvent
        );
    }

    /**
     * Mocks the events that the event reader reads.
     */
    @Before
    public void initEvents() {
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL, ParseTestHelper.createAttributeMock(ID, "modelId"));
    }

    @Test
    public void testParsedRelations() {
        assertThat(relationParser.events.size(), is(0));

        // TODO
        relationParser.parse(xmiFile, nodes);

        assertThat(relationParser.events.size(), is(0));
    }

    @Test
    public void testAnyException() {
        when(modelEvent.asEndElement()).thenThrow(new IllegalArgumentException());
        thrown.expect(ParseException.class);
        thrown.expectCause(is(IllegalArgumentException.class));
        thrown.expectMessage(String.format("The XMI file '%s' could not be parsed.", xmiFile));
        relationParser.parse(xmiFile, nodes);
    }

    @Test
    public void testParseException() {
        when(modelEvent.asEndElement()).thenThrow(new ParseException("Darn!", null));
        thrown.expect(ParseException.class);
        thrown.expectMessage("Darn!");
        relationParser.parse(xmiFile, nodes);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
