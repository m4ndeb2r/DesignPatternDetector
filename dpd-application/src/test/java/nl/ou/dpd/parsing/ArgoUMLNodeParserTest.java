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
 * Tests the {@link ArgoUMLNodeParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLNodeParserTest {

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmiFile = getPath("/argoUML/dummy.xmi");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    XMLEvent modelEvent, classEvent, abstractClassEvent, interfaceEvent,
            datatypeEvent, attributeEvent, operationEvent, parameterEvent;

    private ArgoUMLNodeParser nodeParser;

    /**
     * Initialises the test subject with a mocked xmlInputStream.
     *
     * @throws XMLStreamException not expected.
     */
    @Before
    public void initNodeParser() throws XMLStreamException {
        nodeParser = new ArgoUMLNodeParser(xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
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
                true, // class start-element
                true, // attribute start-element
                true, // datatype start-element
                true, // datatype end-element
                true, // attribute end-element
                true, // operation start-element
                true, // parameter start-element
                true, // parameter end-element
                true, // operation end-element
                true, // class end-element
                true, // abstract class start-element
                true, // abstract class end-element
                true, // interface start-element
                true, // interface end-element
                true, // model end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                classEvent,
                attributeEvent, datatypeEvent, datatypeEvent, attributeEvent,
                operationEvent, parameterEvent, parameterEvent, operationEvent,
                classEvent,
                abstractClassEvent, abstractClassEvent,
                interfaceEvent, interfaceEvent,
                modelEvent);
    }

    /**
     * Mocks the events that the event reader reads.
     */
    @Before
    public void initEvents() {
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL);
        classEvent = ParseTestHelper.createXMLEventMock(CLASS, mockId("classNodeId"));
        operationEvent = ParseTestHelper.createXMLEventMock(OPERATION, mockId("operationId"));
        parameterEvent = ParseTestHelper.createXMLEventMock(PARAMETER, mockId("parameterId"), mockParamKind("in"));
        attributeEvent = ParseTestHelper.createXMLEventMock(ATTRIBUTE, mockId("attributeId"));
        datatypeEvent = ParseTestHelper.createXMLEventMock(DATATYPE, mockHref("...87C"));
        interfaceEvent = ParseTestHelper.createXMLEventMock(INTERFACE, mockId("interfaceNodeId"));
        abstractClassEvent = ParseTestHelper.createXMLEventMock(CLASS, mockId("abstractClassNodeId"), mockIsAbstract(true));
    }

    private javax.xml.stream.events.Attribute mockHref(String href) {
        return ParseTestHelper.createAttributeMock(HREF, href);
    }

    private javax.xml.stream.events.Attribute mockIsAbstract(boolean isAbstract) {
        return ParseTestHelper.createAttributeMock(IS_ABSTRACT, Boolean.toString(isAbstract));
    }

    private javax.xml.stream.events.Attribute mockParamKind(String inOrReturn) {
        return ParseTestHelper.createAttributeMock(KIND, inOrReturn);
    }

    private javax.xml.stream.events.Attribute mockId(String id) {
        return ParseTestHelper.createAttributeMock(ID, id);
    }

    @Test
    public void testParsedNodes() {
        assertThat(nodeParser.events.size(), is(0));

        final Map<String, Node> nodeMap = nodeParser.parse(xmiFile);
        assertThat(nodeMap.keySet().size(), is(4));

        assertClassNode(nodeMap);
        assertAbstractClassNode(nodeMap);
        assertInterface(nodeMap);
        assertDataType(nodeMap);

        assertThat(nodeParser.events.size(), is(0));
    }

    private void assertClassNode(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey("classNodeId"));
        final Node classNode = nodeMap.get("classNodeId");
        assertThat(classNode.getId(), is("classNodeId"));
        assertTrue(classNode.getTypes().contains(NodeType.CONCRETE_CLASS));
        assertAttributes(classNode);
        assertOperations(classNode);
    }

    private void assertAttributes(Node node) {
        assertThat(node.getAttributes().size(), is(1));
        final Attribute attribute = node.getAttributes().iterator().next();
        assertThat(attribute.getId(), is("attributeId"));
        assertThat(attribute.getParentNode().getId(), is(node.getId()));
    }

    private void assertOperations(Node node) {
        assertThat(node.getOperations().size(), is(1));
        final Operation operation = node.getOperations().iterator().next();
        assertThat(operation.getId(), is("operationId"));
        assertThat(operation.getParentNode().getId(), is(node.getId()));
        assertParameter(operation);
    }

    private void assertParameter(Operation operation) {
        assertThat(operation.getParameters().size(), is(1));
        final Parameter parameter = operation.getParameters().iterator().next();
        assertThat(parameter.getId(), is("parameterId"));
        assertThat(parameter.getParentOperation().getId(), is(operation.getId()));
    }

    private void assertAbstractClassNode(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey("abstractClassNodeId"));
        final Node abstractClassNode = nodeMap.get("abstractClassNodeId");
        assertThat(abstractClassNode.getId(), is("abstractClassNodeId"));
        assertThat(abstractClassNode.getTypes().size(), is(2));
        assertTrue(abstractClassNode.getTypes().contains(NodeType.ABSTRACT_CLASS));
        assertTrue(abstractClassNode.getTypes().contains(NodeType.ABSTRACT_CLASS_OR_INTERFACE));
        assertThat(abstractClassNode.getOperations().size(), is(0));
        assertThat(abstractClassNode.getAttributes().size(), is(0));
    }

    private void assertInterface(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey("interfaceNodeId"));
        final Node interfaceNode = nodeMap.get("interfaceNodeId");
        assertThat(interfaceNode.getId(), is("interfaceNodeId"));
        assertThat(interfaceNode.getTypes().size(), is(2));
        assertTrue(interfaceNode.getTypes().contains(NodeType.INTERFACE));
        assertTrue(interfaceNode.getTypes().contains(NodeType.ABSTRACT_CLASS_OR_INTERFACE));
        assertThat(interfaceNode.getAttributes().size(), is(0));
        assertThat(interfaceNode.getOperations().size(), is(0));
    }

    private void assertDataType(Map<String, Node> nodeMap) {
        assertTrue(nodeMap.containsKey("...87C"));
        final Node datatypeNode = nodeMap.get("...87C");
        assertThat(datatypeNode.getId(), is("...87C"));
        assertThat(datatypeNode.getName(), is("Integer"));
        assertThat(datatypeNode.getTypes().size(), is(1));
        assertTrue(datatypeNode.getTypes().contains(NodeType.DATATYPE));
        assertThat(datatypeNode.getAttributes().size(), is(0));
        assertThat(datatypeNode.getOperations().size(), is(0));
    }

    @Test
    public void testAnyException() {
        when(interfaceEvent.asEndElement()).thenThrow(new IllegalArgumentException());
        thrown.expect(ParseException.class);
        thrown.expectCause(is(IllegalArgumentException.class));
        thrown.expectMessage(String.format("The XMI file '%s' could not be parsed.", xmiFile));
        nodeParser.parse(xmiFile);
    }

    @Test
    public void testParseException() {
        when(interfaceEvent.asEndElement()).thenThrow(new ParseException("Darn!", null));
        thrown.expect(ParseException.class);
        thrown.expectMessage("Darn!");
        nodeParser.parse(xmiFile);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
