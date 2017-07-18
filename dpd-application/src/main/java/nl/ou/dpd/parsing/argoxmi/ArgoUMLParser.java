package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.net.URL;

/**
 * A parser for ArgoUML export files (*.xmi). This parser creates an instance of {@link SystemUnderConsideration}
 * containing the information extracted from the ArgoUML export file.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see ArgoUMLNodeParser
 * @see ArgoUMLRelationParser
 * @see SystemRelationsExtractor
 */
public class ArgoUMLParser {

    private final ArgoUMLNodeParser nodeparser;
    private final ArgoUMLRelationParser relationparser;
    private final SystemRelationsExtractor systemRelationsExtractor;

    /**
     * A constructor expecting an {@link XMLInputFactory}.
     *
     * @param xmlInputFactory used for instantiating {@link XMLEventReader}s processing XML files.
     */
    public ArgoUMLParser(XMLInputFactory xmlInputFactory) {
        nodeparser = new ArgoUMLNodeParser(xmlInputFactory);
        relationparser = new ArgoUMLRelationParser(xmlInputFactory);
        systemRelationsExtractor = new SystemRelationsExtractor();
    }

    /**
     * Parses an xmi file with the specified {@code xmiUrl}.
     *
     * @param xmiUrl the url of the file to be parsed.
     * @return a new {@link SystemUnderConsideration}.
     */
    public SystemUnderConsideration parse(URL xmiUrl) {
        return this.parse(xmiUrl.getPath());
    }

    /**
     * Parses an xmi file with the specified {@code xmiFileName}.
     *
     * @param xmiFilename the name of the file to be parsed.
     * @return a new {@link SystemUnderConsideration}.
     */
    public SystemUnderConsideration parse(String xmiFilename) {
        return systemRelationsExtractor.execute(relationparser.parse(xmiFilename, nodeparser.parse(xmiFilename)));
    }

}
