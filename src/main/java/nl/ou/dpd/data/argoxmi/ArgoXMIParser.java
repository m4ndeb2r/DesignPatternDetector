package nl.ou.dpd.data.argoxmi;

/**
 * @author E.M. van Doorn
 */

import nl.ou.dpd.data.parser.Parser;
import nl.ou.dpd.domain.EdgeType;
import nl.ou.dpd.domain.FourTuple;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.exception.DesignPatternDetectorException;
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


public class ArgoXMIParser implements Parser<SystemUnderConsideration> {

    static HashMap<String, ClassElement> classElements;
    static HashMap<String, String> inheritanceElements;
    static ArrayList<AbstractionElement> abstractElements;
    static ArrayList<AssociationElement> associationElements;

    public ArgoXMIParser() {
        classElements = new HashMap();
        inheritanceElements = new HashMap();
        abstractElements = new ArrayList();
        associationElements = new ArrayList();
    }

    public SystemUnderConsideration parse(String fileName) {
        try {
            final ArgoXmiSaxHandler handler = new ArgoXmiSaxHandler();
            final InputStream xmlInput = new FileInputStream(fileName);
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(xmlInput, handler);
            return getFourtuples();
        } catch (SAXException | ParserConfigurationException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden geparsed.", e);
        } catch (IOException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden gevonden.", e);
        }
    }

    private SystemUnderConsideration getFourtuples() {
        final SystemUnderConsideration systemUnderConsideration = new SystemUnderConsideration();

        fourtuplesClassElements(systemUnderConsideration);
        fourtuplesInheritanceElements(systemUnderConsideration);
        fourtuplesAbstractElements(systemUnderConsideration);
        fourtuplesAssociationElements(systemUnderConsideration);

        return systemUnderConsideration;
    }

    private void fourtuplesClassElements(SystemUnderConsideration system) {
        List<String> deps;

        for (String key : classElements.keySet()) {
            deps = classElements.get(key).getDependencies();

            if (deps != null) {
                for (String dep : deps) {
                    final String name = classElements.get(key).getName();
                    final FourTuple ft = new FourTuple(name, dep, EdgeType.DEPENDENCY);
                    system.add(ft);
                }
            }
        }
    }

    private void fourtuplesInheritanceElements(SystemUnderConsideration system) {
        for (String key : inheritanceElements.keySet()) {
            system.add(new FourTuple(
                    ArgoXMIParser.classElements.get(key).getName(),
                    ArgoXMIParser.classElements.get(inheritanceElements.get(key)).getName(),
                    EdgeType.INHERITANCE)
            );
        }
    }

    private void fourtuplesAbstractElements(SystemUnderConsideration system) {
        for (AbstractionElement elem : abstractElements) {
            system.add(new FourTuple(
                    ArgoXMIParser.classElements.get(elem.getImplementer()).getName(),
                    ArgoXMIParser.classElements.get(elem.getSuper()).getName(),
                    EdgeType.INHERITANCE)
            );
        }
    }

    private void fourtuplesAssociationElements(SystemUnderConsideration system) {
        for (AssociationElement assEl : associationElements) {
            system.add(assEl.getFourtuple());
        }
    }
}
