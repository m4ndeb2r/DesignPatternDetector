/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

/**
 *
 * @author E.M. van Doorn
 */

import nl.ou.dpd.fourtuples.FT_constants;
import nl.ou.dpd.fourtuples.FourTuple;
import nl.ou.dpd.fourtuples.FourTupleArray;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ArgoXMI {

    static HashMap<String, ClassElement> classElements;
    static HashMap<String, String> inheritanceElements;
    static ArrayList<AbstractionElement> abstractElements; 
    static ArrayList<AssociationElement> associationElements;
    private String fileName;

    public ArgoXMI(String fn) {
        classElements = new HashMap();
        inheritanceElements = new HashMap();
        abstractElements = new ArrayList();
        associationElements = new ArrayList();

        fileName = fn;
    }

    public void parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            InputStream xmlInput;
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler();

            xmlInput = new FileInputStream(fileName);
            saxParser.parse(xmlInput, handler);

        } catch (Throwable err)
        {
            err.printStackTrace();
            System.exit(1);
        }
    }

    public FourTupleArray getFourtuples() {
        FourTupleArray fta;

        parse();
        
        fta = new FourTupleArray();
        fourtuplesClassElements(fta);
        fourtuplesInheritanceElements(fta);
        fourtuplesAbstractElements(fta);
        fourtuplesAssociationElements(fta);

        return fta;
    }

    void fourtuplesClassElements(FourTupleArray fta) {
        ArrayList<String> dep;

        for (String s : classElements.keySet())
        {
            dep = classElements.get(s).getDependencies();

            if (dep != null)
            {
                for (String nm : dep)
                {
                    fta.add(new FourTuple(classElements.get(s).getName(),
                            nm, FT_constants.DEPENDENCY));
                }
            }
        }
    }

    void fourtuplesInheritanceElements(FourTupleArray fta) {

        for (String s : inheritanceElements.keySet())
        {
            fta.add(new FourTuple(ArgoXMI.classElements.get(s).getName(),
                    ArgoXMI.classElements.get(inheritanceElements.get(s)).getName(),
                    FT_constants.INHERITANCE));
        }
    }

    void fourtuplesAbstractElements(FourTupleArray fta) {
        for (AbstractionElement s : abstractElements)
        {
            fta.add(new FourTuple(ArgoXMI.classElements.get(s.getImplementer()).getName(), 
                    ArgoXMI.classElements.get(s.getSuper()).getName(), 
                    FT_constants.INHERITANCE));
        }
    }

    void fourtuplesAssociationElements(FourTupleArray fta) {
        for (AssociationElement assEl : associationElements)
        {
            fta.add(assEl.getFourtuple());
        }
    }
}
