package nl.ou.dpd.parsing.pattern;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.matching.NodeComparatorFactory;
import nl.ou.dpd.domain.matching.RelationComparatorFactory;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import nl.ou.dpd.parsing.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A parser/validator for XML-files containing design pattern definitions. Parsing results in a list of
 * {@link DesignPattern} objects that are an object representation of the patterns that are defined in the
 * XML-file.
 *
 * @author Martin de Boer
 */
public class PatternsParser {

    private static final Logger LOGGER = LogManager.getLogger(PatternsParser.class);

    private static final String NAME = "name";
    private static final String FAMILY = "family";
    private static final String ID = "id";
    private static final String NODE_1 = "node1";
    private static final String NODE_2 = "node2";
    private static final String NODE_TYPE = "nodeType";
    private static final String RELATION_TYPE = "relationType";
    private static final String CARDINALITY_LEFT = "cardinalityLeft";
    private static final String CARDINALITY_RIGHT = "cardinalityRight";
    private static final String DEFAULT_CARDINALITY = "1";
    private static final String PATTERN = "pattern";
    private static final String NODE = "node";
    private static final String RELATION = "relation";
    private static final String RELATION_RULE = "relation.rule";
    private static final String NODE_RULE = "node.rule";

    private List<DesignPattern> designPatterns = new ArrayList<>();
    private DesignPattern designPattern;
    private Set<Node> nodes;
    private Node node;
    private Relation relation;

    public List<DesignPattern> parse(String xmlFilename) {
        final URL xsdUrl = PatternsParser.class.getResource("/patterns.xsd");
        return parse(xmlFilename, xsdUrl);
    }

    private List<DesignPattern> parse(String xmlFilename, URL xsdUrl) {
        try {
            validate(xmlFilename, xsdUrl);
            doParse(xmlFilename);
        } catch (ParseException pe) {
            // We don't need to repackage a ParseException in a ParseException.
            // Rethrow ParseExceptions directly
            throw pe;
        } catch (Exception e) {
            final String message = String.format("The pattern template file '%s' could not be parsed.", xmlFilename);
            error(message, e);
        }
        return designPatterns;
    }

    private void doParse(String xmlFilename) throws IOException, XMLStreamException {
        try (final InputStream input = new FileInputStream(new File(xmlFilename))) {
            final XMLInputFactory factory = XMLInputFactory.newInstance();
            final XMLEventReader eventReader = factory.createXMLEventReader(input);
            handleEvents(eventReader);
        } catch(Exception ex) {
            throw ex;
        }
    }

    private void validate(String xmlFilename, URL xsdUrl) throws IOException, SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema(xsdUrl);
        final Validator validator = schema.newValidator();
        try(final InputStream stream = new FileInputStream(new File(xmlFilename))) {
            validator.validate(new StreamSource(stream));
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void handleEvents(XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            handleEvent(eventReader.nextEvent());
        }
    }

    private void handleEvent(XMLEvent event) {
        if (event.isStartElement()) {
            handleStartElement(event);
        }
    }

    private void handleStartElement(XMLEvent event) {
        switch (event.asStartElement().getName().getLocalPart()) {
            case PATTERN:
                handlePatternStartElement(event);
                break;
            case NODE:
                handleNodeStartElement(event);
                break;
            case NODE_RULE:
                handleNodeRuleStartElement(event);
                break;
            case RELATION:
                handleRelationStartElement(event);
                break;
            case RELATION_RULE:
                handleRelationRuleStartElement(event);
                break;
            default:
                break;
        }
    }

    private void handlePatternStartElement(XMLEvent event) {
        this.nodes = new HashSet<>();
        this.designPattern = new DesignPattern(getAttributeFromEvent(event, NAME), getAttributeFromEvent(event, FAMILY));
        this.designPatterns.add(designPattern);
        this.designPattern.setNodeComparator(NodeComparatorFactory.createCompoundNodeComparator());
        this.designPattern.setRelationComparator(RelationComparatorFactory.createCompoundRelationComparator());
    }

    private void handleNodeStartElement(XMLEvent event) {
        final String name = getAttributeFromEvent(event, NAME);
        final String id = getAttributeFromEvent(event, ID);
        if (name == null || name.isEmpty()) {
            node = new Node(id, id);
        } else {
            node = new Node(id, name);
        }
        nodes.add(node);
    }

    private void handleRelationStartElement(XMLEvent event) {
        final String id = getAttributeFromEvent(event, ID);
        final Node source = getNodeById(getAttributeFromEvent(event, NODE_1));
        final Node target = getNodeById(getAttributeFromEvent(event, NODE_2));

        if (!designPattern.containsEdge(source, target)) {
            designPattern.addVertex(source);
            designPattern.addVertex(target);
            designPattern.addEdge(source, target)
                    .setId(id)
                    .setName(String.format("%s-%s", source.getName(), target.getName()));
        }
        relation = designPattern.getEdge(source, target);
    }

    /**
     * A node.rule element in the XML is converted to a node type in the {@link DesignPattern} object.
     *
     * @param event the XML event of the rule element
     */
    private void handleNodeRuleStartElement(XMLEvent event) {
        node.addType(NodeType.valueOf(getAttributeFromEvent(event, NODE_TYPE)));
    }

    /**
     * A relation.rule element in the XML is converted to a relation characteristic in the {@link DesignPattern} object.
     *
     * @param event the XML event of the rule element
     */
    private void handleRelationRuleStartElement(XMLEvent event) {
        final RelationType type = RelationType.valueOf(getAttributeFromEvent(event, RELATION_TYPE));
        final Cardinality cardinalityLeft = getCardinality(event, CARDINALITY_LEFT);
        final Cardinality cardinalityRight = getCardinality(event, CARDINALITY_RIGHT);
        relation.addRelationProperty(new RelationProperty(type, cardinalityLeft, cardinalityRight));
    }

    private Cardinality getCardinality(XMLEvent event, String elementName) {
        final String elementValue = getAttributeFromEvent(event, elementName);
        if (elementValue == null) {
            return Cardinality.valueOf(DEFAULT_CARDINALITY);
        } else {
            return Cardinality.valueOf(elementValue);
        }
    }

    private Node getNodeById(String nodeId) {
        return nodes.stream()
                .filter(node -> node.getId().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    private String getAttributeFromEvent(XMLEvent event, String attributeName) {
        final Iterator<Attribute> attrIterator = event.asStartElement().getAttributes();
        while (attrIterator.hasNext()) {
            Attribute a = attrIterator.next();
            if (a.getName().getLocalPart().equals(attributeName)) {
                return a.getValue();
            }
        }
        return null;
    }

    private void error(String message, Exception cause) {
        LOGGER.error(message);
        throw new ParseException(message, cause);
    }

}

