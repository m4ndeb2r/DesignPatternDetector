package nl.ou.dpd.data.template;


import nl.ou.dpd.data.parser.Parser;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A parser for the design templates files.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class TemplatesParser implements Parser<List<DesignPattern>> {

    /**
     * Parses a template file with the specified {@code filename}.
     *
     * @param filename the name of the file to be parsed.
     * @return a list of {@link DesignPattern}s.
     */
    public List<DesignPattern> parse(String filename) {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            final TemplateSaxHandler handler = new TemplateSaxHandler();
            final InputStream xmlInput = new FileInputStream(filename);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlInput, handler);
            return handler.getTemplates();
        } catch (SAXException | ParserConfigurationException e) {
            throw new DesignPatternDetectorException("Het bestand " + filename + " kon niet worden geparsed.", e);
        } catch (IOException e) {
            throw new DesignPatternDetectorException("Het bestand " + filename + " kon niet worden gevonden.", e);
        }
    }
}
