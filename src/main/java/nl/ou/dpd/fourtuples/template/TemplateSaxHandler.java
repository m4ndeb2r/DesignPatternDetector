/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.fourtuples.template;

/**
 *
 * @author E.M. van Doorn
 */

import nl.ou.dpd.fourtuples.FourTupleArray;
import nl.ou.dpd.sax.ElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class TemplateSaxHandler extends DefaultHandler {
     private ElementHandler handler;
     private TemplateElement templateElement;
     ArrayList<FourTupleArray> dps;

    public TemplateSaxHandler() {
        handler = null;
        dps = new ArrayList();
    }
    
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException { 
        
        if (handler != null)
        {
            handler.startElement(qName, attributes);

            return;
        }
        
        switch (qName)
        {
            case "templates":
                break;
                
            case "template":
                templateElement = new TemplateElement();
                handler = templateElement;
                handler.startElement(qName, attributes);
                break;
                
            default:    
                System.out.println("Unexpected tag: " + qName);
                break;
        }
    }


    public void endElement(String uri, String localName,
            String qName) throws SAXException {

        if (handler != null)
        {
            handler = handler.endElement(qName);
        }
        
        if (qName.equals("template"))
        {
            dps.add(templateElement.getTemplate());
        }
    }
    
    
    public ArrayList<FourTupleArray> getTemplates()
    {        
        return dps;
    }
}
