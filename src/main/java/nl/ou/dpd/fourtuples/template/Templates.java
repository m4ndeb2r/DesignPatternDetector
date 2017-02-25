package nl.ou.dpd.fourtuples.template;


import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.fourtuples.FourTupleArray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A parser for the design templates files.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Templates {
    private String fileName;

    /**
     * Constructor that expects the name of a templates input file, containing the design patterns specification.
     *
     * @param fn the name of the templates file to be parsed.
     */
    public Templates(String fn) {
        fileName = fn;
    }

    /**
     * Parses the template file, for which the filename was passed to the constructor.
     *
     * @return a list of {@link FourTupleArray} instances representing the specified design patters.
     */
    public ArrayList<FourTupleArray> parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            final TemplateSaxHandler handler = new TemplateSaxHandler();
            final InputStream xmlInput = new FileInputStream(fileName);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlInput, handler);
            return handler.getTemplates();
        } catch (SAXException | ParserConfigurationException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden geparsed.", e);
        } catch (IOException e) {
            throw new DesignPatternDetectorException("Het bestand " + fileName + " kon niet worden gevonden.", e);
        }
    }
}
