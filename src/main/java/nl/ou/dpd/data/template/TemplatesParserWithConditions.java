/**
 * 
 */
package nl.ou.dpd.data.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.Condition;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Operator;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;
import nl.ou.dpd.exception.DesignPatternDetectorException;

/**
 * This class implements a parser for a template.xml with a structure- and conditions part, including parsing nodes, edges, attributes and conditions.
 * TODO: implement parsing of methods.   
 * 
 * @author Peter Vansweevelt
 *
 */
public class TemplatesParserWithConditions {
	
    private static final Logger LOGGER = LogManager.getLogger(TemplatesParserWithConditions.class);
    
    private List<DesignPattern> designPatterns;
    private DesignPattern designPattern;
    private List<Condition> conditions;
    private List<Node> nodes;
/*    private List<Map<String, String>> attributeParameters;
    private List<List<Map<String, String>>> attributeParametersPerNode;
*/
    private Map<XMLEvent, Node> attributeEventsPerNode;
    /**
     * Parses a template file with the specified {@code filename}.
     *
     * @param filename the name of the file to be parsed.
     * @return a list of {@link DesignPattern}s.
     */
    public List<DesignPattern> parse(String filename) {
 
		//initialize lists
		designPatterns = new ArrayList<DesignPattern>();
		nodes = new ArrayList<Node>();
		conditions = new ArrayList<Condition>();
		attributeEventsPerNode = new HashMap<XMLEvent, Node>();
/*		attributeParameters  = new ArrayList<Map<String, String>>();
		attributeParametersPerNode = new ArrayList<List<Map<String, String>>>();
*/		
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	
    	try {
	    	InputStream input = new FileInputStream(new File(filename));
	        XMLEventReader eventReader = factory.createXMLEventReader(input);	        
	        handleEvents(eventReader);
	    } catch (XMLStreamException e) {
            final String msg = "The pattern template file " + filename + " could not be parsed.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        } catch (FileNotFoundException e) {
            final String msg = "The pattern template file " + filename + " could not be found.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        }
    	
    	return designPatterns;
    }

	/**
	 * General method to iterate the events of the XML.
	 * @param eventReader
	 * @throws XMLStreamException 
	 */
	private void handleEvents(XMLEventReader eventReader) throws XMLStreamException {
	    while (eventReader.hasNext()) {
	    	XMLEvent event = eventReader.nextEvent();
	    	handleEvent(event);
	    }        
	}

	/**
	 * General method to handle events
	 * @param event
	 */
	private void handleEvent(XMLEvent event) {
		if (event.isStartElement()) {
			switch (event.asStartElement().getName().getLocalPart()) {
				case TemplateTag.TEMPLATES:
					break;
				case TemplateTag.TEMPLATE:
					//create a new template and add it to the templates list 
					designPattern = createTemplate(readAttributes(event));
					designPatterns.add(designPattern);
					//empty lists for the new template
					nodes.clear();
					conditions.clear();
					attributeEventsPerNode.clear();
					break;
				case TemplateTag.STRUCTURE:
					break;
				case TemplateTag.NODES:
 					break;
				case TemplateTag.NODE:
					//add a new node to the nodes list
					Node node = createNode(readAttributes(event));
					if (!nodeIdIsUnique(node)) {
						final String msg = "The node id " + node.getId() + " is not unique in this pattern.";
			            final Exception e = new XMLStreamException();
			            LOGGER.error(msg, e);
			 		    throw new DesignPatternDetectorException(msg, e);
					}
					nodes.add(node);
					//make a condition concerning the node type and add it to the conditions list
					Condition condition = makeNodeTypeCondition(node);
					conditions.add(condition);
					break;
				case TemplateTag.ATTRIBUTE:
					//Keep the attribute event with its corresponding node
					attributeEventsPerNode.put(event, nodes.get(nodes.size() - 1));
					break;
				case TemplateTag.METHOD:
					break;
				case TemplateTag.EDGES:
					break;
				case TemplateTag.EDGE:
					//add a new edge to the designPattern
					Edge edge = createEdge(readAttributes(event));
					if (!edgeIdIsUnique(edge)) {
						final String msg = "The edge id " + edge.getId() + " is not unique in this pattern.";
			            final Exception e = new XMLStreamException();
			            LOGGER.error(msg, e);
			 		    throw new DesignPatternDetectorException(msg, e);
					}
					designPattern.addRealEdge(edge);
					break;
				case TemplateTag.CONDITIONS:
					break;
				case TemplateTag.CONDITION:
					//make a condition and add it to the conditions list
					condition = makeCondition(readAttributes(event));
					conditions.add(condition);
					break;
				case TemplateTag.RULE:
					//make a rule and add it to the condition
					Rule rule = makeRule(readAttributes(event));
					addRuleToCondition(rule, conditions.get(conditions.size() - 1));
					//TODO apply rule on designpattern node or edge
					applyRule(rule, readAttributes(event).get(TemplateAttribute.VALUE));
					break;
				default:
		            final String msg = "The pattern template tag " + event.asStartElement().getName().getLocalPart() + " could not be handled.";
		            final Exception e = new XMLStreamException();
		            LOGGER.error(msg, e);
		 		    throw new DesignPatternDetectorException(msg, e);					
			}
		}
		
		if (event.isEndElement()) {
			switch (event.asEndElement().getName().getLocalPart()) {
				case TemplateTag.NODES:
					//make attributes and add them to the corresponding nodes using the attributeEventsPerNode Map
					createAttributes();
					break;
				case TemplateTag.EDGES:
					//make attribute exist conditions
					makeAttributeExistsConditions();
					break;
			}
		}	
	}

	/**
	 * General method to read attributes
	 * @param event
	 * @return a Map<String, String> holding the attributes with the attributename as key and the attributevalue as value
	 */
	private Map<String, String> readAttributes(XMLEvent event) {
		//holds the attributes with the attributename as key and the attributevalue as value
		Map<String, String> attributes = new HashMap<String, String>();
		Iterator<Attribute> attrIterator = event.asStartElement().getAttributes();
		while (attrIterator.hasNext()) {
			Attribute attr = (Attribute) attrIterator.next();
			attributes.put(attr.getName().getLocalPart(), attr.getValue());			
		}
		return attributes;		
	}
	
	/**
	 * @param attributes
	 * @return a new DesignPattern with the specified name.
	 */
	private DesignPattern createTemplate(Map<String, String> attributes) {
		String name = attributes.get(TemplateAttribute.NAME);
		return new DesignPattern(name);		
	}

	/**
	 * @param attributes
	 * @return a new Node with the specified name and type.
	 */
	private Node createNode(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String type = attributes.get(TemplateAttribute.TYPE);
		if (type.equalsIgnoreCase("class")) {
			return new Clazz(id, id);
		}
		if (type.equalsIgnoreCase("interface")) {
			return new Interface(id, id);
		}
		return null;
	}
	
	/**
	 * @param node
	 * @return a new DesignPattern with the specified name.
	 */
	private Condition makeNodeTypeCondition(Node node) {
		String id = "SystemCondition " + conditions.size() + 1;
		String description = "The node " + node.getName() + " must be of type "  + node.getType().toString(); 
		Condition c = new Condition(id, description);
		NodeRule rule = new NodeRule(node, Topic.TYPE, Scope.OBJECT, Operator.EQUALS);
		c.getNodeRules().add(rule);
		return c;		
	}

	/**
	 * Checks if the id of the new node is unique in this pattern.
	 * @param node
	 * @return true if the node is unique in this pattern. False otherwise.
	 */
	private boolean nodeIdIsUnique(Node node) {
		for (Node n : nodes) {
			if (n.getId().equals(node.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the id of the new edge is unique in this pattern.
	 * @param edge
	 * @return true if the edge is unique in this pattern. False otherwise.
	 */
	private boolean edgeIdIsUnique(Edge edge) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getId().equals(edge.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create all the attributes and add them to the corresponding nodes.
	 */
	private void createAttributes() {
		Iterator attrIterator = attributeEventsPerNode.keySet().iterator();
		while (attrIterator.hasNext()) {
			XMLEvent event = (XMLEvent) attrIterator.next();
			Map <String, String> parameters = readAttributes(event);
			nl.ou.dpd.domain.node.Attribute attribute = createAttribute(parameters);
			
			Node node = attributeEventsPerNode.get(event);
			node.getAttributes().add(attribute);
		}		
	}

	/**
	 * @param attributes
	 * @return a new Attribute with the specified name and type (found in the list of Nodes).
	 */
	private nl.ou.dpd.domain.node.Attribute createAttribute(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String type = attributes.get(TemplateAttribute.TYPE);
		Node node = findNodeById(type);
		nl.ou.dpd.domain.node.Attribute attr = new nl.ou.dpd.domain.node.Attribute(id, node);
		return attr;
	}

	/**
	 * @param type the type as a String
	 * @return the node object with the name of 'type'
	 */
	private Node findNodeById(String id) {
		for (Node n : nodes) {
			if (n.getId().equals(id)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * @param type the type as a String
	 * @return the node object with the name of 'type'
	 */
	private nl.ou.dpd.domain.node.Attribute findAttributeById(String id) {
		for (Node n : nodes) {
			for (nl.ou.dpd.domain.node.Attribute attr : n.getAttributes()) {
				if (attr.getId().equals(id)) {
					return attr;
				}
			}
		}
		return null;
	}

	/**
	 * @param type the type as a String
	 * @return the node object with the name of 'type'
	 */
	private Edge findEdgeById(String id) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * @param attributes
	 * @return a new Node with the specified name and type.
	 */
	private Edge createEdge(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String node1Name = attributes.get(TemplateAttribute.NODE1);
		String node2Name = attributes.get(TemplateAttribute.NODE2);
		Node node1 = findNodeById(node1Name);
		Node node2 = findNodeById(node2Name);		
		return new Edge(id, id, node1, node2);
	}
	/**
	 * @param node
	 * @return condition
	 */
	private Condition makeAttributeExistsConditions() {
		for(Node node : nodes) {
			for (nl.ou.dpd.domain.node.Attribute attr : node.getAttributes()) {
				makeAttributeExistsCondition(node, attr);
			}
		}
	return null;
	}
	
	/**
	 * @param node
	 * @return condition
	 */
	private Condition makeAttributeExistsCondition(Node node, nl.ou.dpd.domain.node.Attribute attr) {
		
		Node attributeType = attr.getType();		
		String id = "SystemCondition " + conditions.size() + 1;
		String description = "The node " + node.getName() + " must have an attribute of type "  + attributeType.getId();
		Condition condition = new Condition(id, description);

		Edge edge = findEdgeWithTwoNodes(node, attributeType);
		if (edge == null) {
			final String msg = "An edge between " + node.getId() + " and " + attributeType.getId() + " could not be found.";
            final Exception e = new XMLStreamException();
            LOGGER.error(msg, e);
 		    throw new DesignPatternDetectorException(msg, e);			
		}
		EdgeRule rule = new EdgeRule(edge, Topic.TYPE, Scope.ATTRIBUTE, Operator.EXISTS);
		condition.getEdgeRules().add(rule);		
		return condition;		
	}

	private Edge findEdgeWithTwoNodes(Node leftNode, Node rightNode) {
		for (Edge e : designPattern.getEdges()) {
			if (e.getLeftNode() == leftNode && e.getRightNode() == rightNode) {
				return e;
			}
		}
		return null;
	}

	/**
	 * @param readAttributes
	 * @return
	 */
	private Condition makeCondition(Map<String, String> attributes) {
		String id = attributes.get(TemplateAttribute.ID);
		String description = attributes.get(TemplateAttribute.DESCRIPTION);
		return new Condition(id, description);
	}
	
	/**
	 * @param readAttributes
	 * @return
	 */
	private Rule makeRule(Map<String, String> attributes) {
		String applies = attributes.get(TemplateAttribute.APPLIES);
		String scope = attributes.get(TemplateAttribute.SCOPE);
		String topic = attributes.get(TemplateAttribute.TOPIC);
		String operator = attributes.get(TemplateAttribute.OPERATOR);
		String not = attributes.get(TemplateAttribute.NOT);
		
		if (findNodeById(applies) != null) {
			//a nodeRule
			return new NodeRule(findNodeById(applies), findTopic(topic) , findScope(scope), findOperator(operator, not));						
		}
		if (findEdgeById(applies) != null) {
			//an edgeRule
			return new EdgeRule(findEdgeById(applies), findTopic(topic) , findScope(scope), findOperator(operator, not));						
		}
		if (findAttributeById(applies) != null) {
			//an attributeRule
			return new AttributeRule(findAttributeById(applies), findTopic(topic) , findScope(scope), findOperator(operator, not));						
		}

		return null;
	}

	public Scope findScope(String scope) {
		if (scope.equalsIgnoreCase("RELATION")) {
				return Scope.RELATION;
		}
		if (scope.equalsIgnoreCase("OBJECT")) {
			return Scope.OBJECT;
		}
		if (scope.equalsIgnoreCase("ATTRIBUTE")) {
			return Scope.ATTRIBUTE;
		}
		return null;		
	}
	
	public Topic findTopic(String topic) {
		if (topic.equalsIgnoreCase("TYPE")) {
			return Topic.TYPE;
		}
		if (topic.equalsIgnoreCase("VISIBILITY")) {
			return Topic.VISIBILITY;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ROOT")) {
			return Topic.MODIFIER_ROOT;
		}
		if (topic.equalsIgnoreCase("MODIFIER_LEAF")) {
			return Topic.MODIFIER_LEAF;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ABSTRACT")) {
			return Topic.MODIFIER_ABSTRACT;
		}
		if (topic.equalsIgnoreCase("MODIFIER_ACTIVE")) {
			return Topic.MODIFIER_ACTIVE;
		}
		if (topic.equalsIgnoreCase("CARDINALITY-FRONT")) {
			return Topic.CARDINALITY_LEFT;
		}
		if (topic.equalsIgnoreCase("CARDINALITY-END")) {
			return Topic.CARDINALITY_RIGHT;
		}
		return null;
	}
	
	public Operator findOperator(String operator, String not) {
		if (not == null || not == "") not = "false";
		
		if (operator.equalsIgnoreCase("EQUALS") && not.equalsIgnoreCase("FALSE")) {
				return Operator.EQUALS;
		}
		if (operator.equalsIgnoreCase("NOT_EQUALS") && not.equalsIgnoreCase("TRUE")) {
			return Operator.NOT_EQUALS;
		}
		if (operator.equalsIgnoreCase("EXISTS") && not.equalsIgnoreCase("FALSE")) {
			return Operator.EXISTS;
		}
		if (operator.equalsIgnoreCase("NOT_EXISTS") && not.equalsIgnoreCase("TRUE")) {
			return Operator.NOT_EXISTS;
		}
		return null;		
	}
	
	/**
	 * @param rule
	 * @param condition
	 */
	private void addRuleToCondition(Rule rule, Condition condition) {
		if (rule instanceof NodeRule) {
			condition.getNodeRules().add((NodeRule) rule);
		}
		if (rule instanceof EdgeRule) {
			condition.getEdgeRules().add((EdgeRule) rule);
		}
		if (rule instanceof AttributeRule) {
			condition.getAttributeRules().add((AttributeRule) rule);
		}
	}

	/**
	 * @param rule
	 * @param value
	 */
	private void applyRule(Rule rule, String value) {
		if (rule instanceof NodeRule) {
			ApplyRule applyRule = new ApplyNodeRule(rule, value);
			applyRule.apply();
		}
		if (rule instanceof EdgeRule) {
			ApplyRule applyRule = new ApplyEdgeRule(rule, value);
			applyRule.apply();
		}
		if (rule instanceof AttributeRule) {
			ApplyRule applyRule = new ApplyAttributeRule(rule, value);
			applyRule.apply();
		}
	}
}
