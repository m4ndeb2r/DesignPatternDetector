package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.data.parser.Parser;
import nl.ou.dpd.domain.EdgeType;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.SystemUnderConsiderationClass;
import nl.ou.dpd.domain.SystemUnderConsiderationEdge;
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
 * is a {@link SystemUnderConsiderationEdge} instance.
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
            final String msg = "Het bestand " + fileName + " kon niet worden geparsed.";
            LOGGER.error(msg);
            throw new DesignPatternDetectorException(msg, e);
        } catch (IOException e) {
            final String msg = "Het bestand " + fileName + " kon niet worden gevonden.";
            LOGGER.error(msg);
            throw new DesignPatternDetectorException(msg, e);
        }
    }

    private SystemUnderConsideration getSystemUnderConsideration() {
        final SystemUnderConsideration systemUnderConsideration = new SystemUnderConsideration();

        fourtuplesClassElements(systemUnderConsideration);
        fourtuplesInheritanceElements(systemUnderConsideration);
        fourtuplesAbstractElements(systemUnderConsideration);
        fourtuplesAssociationElements(systemUnderConsideration);

        return systemUnderConsideration;
    }

    private void fourtuplesClassElements(SystemUnderConsideration system) {
        for (String key : classElements.keySet()) {
            final List<String> dependencies = classElements.get(key).getDependencies();
            if (dependencies != null) {
                for (String dep : dependencies) {
                    final String name = classElements.get(key).getName();
                    final SystemUnderConsiderationClass class1 = new SystemUnderConsiderationClass(name);
                    final SystemUnderConsiderationClass class2 = new SystemUnderConsiderationClass(dep);
                    system.add(new SystemUnderConsiderationEdge(class1, class2, EdgeType.DEPENDENCY));
                }
            }
        }
    }

    private void fourtuplesInheritanceElements(SystemUnderConsideration system) {
        for (String key : inheritanceElements.keySet()) {
            final String name1 = ArgoXMIParser.classElements.get(key).getName();
            final String name2 = ArgoXMIParser.classElements.get(inheritanceElements.get(key)).getName();
            final SystemUnderConsiderationClass class1 = new SystemUnderConsiderationClass(name1);
            final SystemUnderConsiderationClass class2 = new SystemUnderConsiderationClass(name2);
            system.add(new SystemUnderConsiderationEdge(class1, class2, EdgeType.INHERITANCE));
        }
    }

    private void fourtuplesAbstractElements(SystemUnderConsideration system) {
        for (AbstractionElement elem : abstractElements) {
            final String name1 = ArgoXMIParser.classElements.get(elem.getImplementer()).getName();
            final String name2 = ArgoXMIParser.classElements.get(elem.getSuper()).getName();
            final SystemUnderConsiderationClass class1 = new SystemUnderConsiderationClass(name1);
            final SystemUnderConsiderationClass class2 = new SystemUnderConsiderationClass(name2);
            system.add(new SystemUnderConsiderationEdge(class1, class2, EdgeType.INHERITANCE));
        }
    }

    private void fourtuplesAssociationElements(SystemUnderConsideration system) {
        for (AssociationElement elem : associationElements) {
            system.add(elem.getSystemUnderConsiderationEdge());
        }
    }
}
