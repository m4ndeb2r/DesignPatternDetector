/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

/**
 * @author E.M. van Doorn
 */

import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.fourtuples.EdgeType;
import nl.ou.dpd.fourtuples.FourTuple;
import nl.ou.dpd.fourtuples.FourTupleArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ArgoXMI {

    static HashMap<String, ClassElement> classElements;
    static HashMap<String, String> inheritanceElements;
    static ArrayList<AbstractionElement> abstractElements;
    static ArrayList<AssociationElement> associationElements;

    public ArgoXMI(String fileName) {
        classElements = new HashMap();
        inheritanceElements = new HashMap();
        abstractElements = new ArrayList();
        associationElements = new ArrayList();

        parse(fileName);
    }

    public FourTupleArray getFourtuples() {
        final FourTupleArray fta = new FourTupleArray();

        fourtuplesClassElements(fta);
        fourtuplesInheritanceElements(fta);
        fourtuplesAbstractElements(fta);
        fourtuplesAssociationElements(fta);

        return fta;
    }

    private void parse(String fileName) {
        try {
            final ArgoXmiSaxHandler handler = new ArgoXmiSaxHandler();
            final InputStream xmlInput = new FileInputStream(fileName);
            final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(xmlInput, handler);
        } catch (SAXException | ParserConfigurationException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden geparsed.", e);
        } catch (IOException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden gevonden.", e);
        }
    }

    private void fourtuplesClassElements(FourTupleArray fta) {
        ArrayList<String> deps;

        for (String key : classElements.keySet()) {
            deps = classElements.get(key).getDependencies();

            if (deps != null) {
                for (String dep : deps) {
                    final String name = classElements.get(key).getName();
                    final FourTuple ft = new FourTuple(name, dep, EdgeType.DEPENDENCY);
                    fta.add(ft);
                }
            }
        }
    }

    private void fourtuplesInheritanceElements(FourTupleArray fta) {

        for (String key : inheritanceElements.keySet()) {
            fta.add(new FourTuple(ArgoXMI.classElements.get(key).getName(),
                    ArgoXMI.classElements.get(inheritanceElements.get(key)).getName(),
                    EdgeType.INHERITANCE));
        }
    }

    private void fourtuplesAbstractElements(FourTupleArray fta) {
        for (AbstractionElement elem : abstractElements) {
            fta.add(new FourTuple(ArgoXMI.classElements.get(elem.getImplementer()).getName(),
                    ArgoXMI.classElements.get(elem.getSuper()).getName(),
                    EdgeType.INHERITANCE));
        }
    }

    private void fourtuplesAssociationElements(FourTupleArray fta) {
        for (AssociationElement assEl : associationElements) {
            fta.add(assEl.getFourtuple());
        }
    }
}
