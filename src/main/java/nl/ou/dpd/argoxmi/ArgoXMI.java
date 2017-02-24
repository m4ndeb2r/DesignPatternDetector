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
import nl.ou.dpd.fourtuples.FT_constants;
import nl.ou.dpd.fourtuples.FourTuple;
import nl.ou.dpd.fourtuples.FourTupleArray;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
            InputStream xmlInput;
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            ArgoXmiSaxHandler handler = new ArgoXmiSaxHandler();
            xmlInput = new FileInputStream(fileName);
            saxParser.parse(xmlInput, handler);
        } catch (SAXException | ParserConfigurationException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden geparsed.", e);
        } catch (IOException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden gevonden.", e);
        }
    }

    private void fourtuplesClassElements(FourTupleArray fta) {
        ArrayList<String> dep;

        for (String s : classElements.keySet()) {
            dep = classElements.get(s).getDependencies();

            if (dep != null) {
                for (String nm : dep) {
                    fta.add(new FourTuple(classElements.get(s).getName(),
                            nm, FT_constants.DEPENDENCY));
                }
            }
        }
    }

    private void fourtuplesInheritanceElements(FourTupleArray fta) {

        for (String s : inheritanceElements.keySet()) {
            fta.add(new FourTuple(ArgoXMI.classElements.get(s).getName(),
                    ArgoXMI.classElements.get(inheritanceElements.get(s)).getName(),
                    FT_constants.INHERITANCE));
        }
    }

    private void fourtuplesAbstractElements(FourTupleArray fta) {
        for (AbstractionElement s : abstractElements) {
            fta.add(new FourTuple(ArgoXMI.classElements.get(s.getImplementer()).getName(),
                    ArgoXMI.classElements.get(s.getSuper()).getName(),
                    FT_constants.INHERITANCE));
        }
    }

    private void fourtuplesAssociationElements(FourTupleArray fta) {
        for (AssociationElement assEl : associationElements) {
            fta.add(assEl.getFourtuple());
        }
    }
}
