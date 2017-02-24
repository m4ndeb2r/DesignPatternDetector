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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Templates {
    private String fileName;
    
    public Templates(String fn)
    {
        fileName = fn;
    }
    
    public ArrayList<FourTupleArray> parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            InputStream xmlInput;
            SAXParser saxParser = factory.newSAXParser();
            TemplateSaxHandler handler = new TemplateSaxHandler();

            xmlInput = new FileInputStream(fileName);
            saxParser.parse(xmlInput, handler);
            
            return handler.getTemplates();

        } catch (Throwable err)
        {
            err.printStackTrace();
            System.exit(1);
        }
        
        return null;
    } 
}
