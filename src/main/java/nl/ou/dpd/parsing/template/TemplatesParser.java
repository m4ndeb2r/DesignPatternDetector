package nl.ou.dpd.parsing.template;


import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.parsing.ParseException;
import nl.ou.dpd.parsing.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(TemplatesParser.class);

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
            final String msg = "The pattern template file " + filename + " could not be parsed.";
            LOGGER.error(msg, e);
            throw new ParseException(msg, e);
        } catch (IOException e) {
            final String msg = "The pattern template file " + filename + " could not be found.";
            LOGGER.error(msg, e);
            throw new ParseException(msg, e);
        }
    }
}
