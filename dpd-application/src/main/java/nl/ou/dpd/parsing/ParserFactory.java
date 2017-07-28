package nl.ou.dpd.parsing;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.validation.SchemaFactory;

/**
 * A factory for creating parsers.
 *
 * @author Martin de Boer
 */
public class ParserFactory {

    /**
     * Creates an {@link ArgoUMLParser}, consisting of an {@link ArgoUMLNodeParser}, an {@link ArgoUMLRelationParser}
     * and a {@link SystemRelationsExtractor}.
     *
     * @return the created {@link ArgoUMLParser}
     */
    public static ArgoUMLParser createArgoUMLParser() {
        final ArgoUMLNodeParser nodeParser = new ArgoUMLNodeParser(XMLInputFactory.newInstance());
        final ArgoUMLRelationParser relationParser = new ArgoUMLRelationParser(XMLInputFactory.newInstance());
        final SystemRelationsExtractor systemRelationsExtractor = new SystemRelationsExtractor();
        return new ArgoUMLParser(nodeParser, relationParser, systemRelationsExtractor);
    }

    /**
     * Creates a {@link PatternsParser} having a {@link SchemaFactory} (for XSD validation) and an
     * {@link XMLInputFactory} for creating an XML input stream.
     *
     * @return the created {@link PatternsParser}
     */
    public static PatternsParser createPatternParser() {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        return new PatternsParser(schemaFactory, xmlInputFactory);
    }
}
