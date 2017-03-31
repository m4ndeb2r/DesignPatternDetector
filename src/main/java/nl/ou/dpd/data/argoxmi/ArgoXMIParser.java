package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.data.parser.Parser;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for the XMI input files, containing a representation of the "system under consideration". The parsing result
 * is a {@link SystemUnderConsideration} instance.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class ArgoXMIParser implements Parser<SystemUnderConsideration> {

    private static final Logger LOGGER = LogManager.getLogger(ArgoXMIParser.class);

    static Map<String, ClassElement> classElements;
    static Map<String, String> inheritanceElements;
    static ArrayList<AbstractionElement> abstractElements;
    static ArrayList<AssociationElement> associationElements;

    /**
     * Default constructor that initialises the static attributes {@link #classElements}, {@link #inheritanceElements},
     * {@link #abstractElements} and {@link #associationElements}.
     */
    public ArgoXMIParser() {
        classElements = new HashMap();
        inheritanceElements = new HashMap();
        abstractElements = new ArrayList();
        associationElements = new ArrayList();
    }

    /**
     * {@inheritDoc}
     */
    public SystemUnderConsideration parse(String fileName) {
        try {
            final ArgoXmiSaxHandler handler = new ArgoXmiSaxHandler();
            final InputStream xmlInput = new FileInputStream(fileName);
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(xmlInput, handler);
            return getSystemUnderConsideration();
        } catch (SAXException | ParserConfigurationException e) {
            final String msg = "The file " + fileName + " could not be parsed.";
            LOGGER.error("***" + msg, e);
            throw new DesignPatternDetectorException(msg, e);
        } catch (IOException e) {
            final String msg = "The file " + fileName + " could not be found.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        }
    }

    /**
     * Adds the parsed elements to a {@link SystemUnderConsideration} and returns the result.
     *
     * @return a populated {@link SystemUnderConsideration} instance.
     */
    private SystemUnderConsideration getSystemUnderConsideration() {
        final SystemUnderConsideration systemUnderConsideration = new SystemUnderConsideration();

        addClassElements(systemUnderConsideration);
        addInheritanceElements(systemUnderConsideration);
        addAbstractElements(systemUnderConsideration);
        addAssociationElements(systemUnderConsideration);

        return systemUnderConsideration;
    }

    /**
     * Adds class elements to the specified {@link SystemUnderConsideration}.
     *
     * @param system the {@link SystemUnderConsideration} to add the class elements to.
     */
    private void addClassElements(SystemUnderConsideration system) {
        for (String key : classElements.keySet()) {
            final List<String> dependencies = classElements.get(key).getDependencies();
            if (dependencies != null) {
                for (String dep : dependencies) {
                    final String name = classElements.get(key).getName();
                    final Clazz class1 = new Clazz(name);
                    final Clazz class2 = new Clazz(dep);
                    system.add(new Edge(class1, class2, EdgeType.DEPENDENCY));
                }
            }
        }
    }

    /**
     * Adds inheritance elements to the specified {@link SystemUnderConsideration}.
     *
     * @param system the {@link SystemUnderConsideration} to add the inheritance elements to.
     */
    private void addInheritanceElements(SystemUnderConsideration system) {
        for (String key : inheritanceElements.keySet()) {
            final String name1 = ArgoXMIParser.classElements.get(key).getName();
            final String name2 = ArgoXMIParser.classElements.get(inheritanceElements.get(key)).getName();
            final Clazz class1 = new Clazz(name1);
            final Clazz class2 = new Clazz(name2);
            system.add(new Edge(class1, class2, EdgeType.INHERITANCE));
        }
    }

    /**
     * Adds abstract elements to the specified {@link SystemUnderConsideration}.
     *
     * @param system the {@link SystemUnderConsideration} to add the inheritance elements to.
     */
    private void addAbstractElements(SystemUnderConsideration system) {
        for (AbstractionElement elem : abstractElements) {
            final String name1 = ArgoXMIParser.classElements.get(elem.getImplementer()).getName();
            final String name2 = ArgoXMIParser.classElements.get(elem.getSuper()).getName();
            final Clazz class1 = new Clazz(name1);
            final Clazz class2 = new Clazz(name2);
            system.add(new Edge(class1, class2, EdgeType.INHERITANCE));
        }
    }

    /**
     * Adds association elements to the specified {@link SystemUnderConsideration}.
     *
     * @param system the {@link SystemUnderConsideration} to add the association elements to.
     */
    private void addAssociationElements(SystemUnderConsideration system) {
        for (AssociationElement elem : associationElements) {
            system.add(elem.getEdge());
        }
    }
}
