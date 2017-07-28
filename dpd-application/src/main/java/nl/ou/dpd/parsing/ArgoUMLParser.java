package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;

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
     * Creates a (compound) parser, consisting of the specified {@link ArgoUMLNodeParser}, {@link ArgoUMLRelationParser}
     * and {@link SystemRelationsExtractor}.
     * <p>
     * This constructor has package protected access so it can only be instantiated from within the same package (by the
     * ParserFactory or in a unit test in the same package).
     *
     * @param nodeparser               a parser for the nodes in the ArgoUML input xmi
     * @param relationparser           a parser for the relations in the ArgoUML input xmi
     * @param systemRelationsExtractor the system relation extractor performing a kind of post-parsing
     */
    ArgoUMLParser(
            ArgoUMLNodeParser nodeparser,
            ArgoUMLRelationParser relationparser,
            SystemRelationsExtractor systemRelationsExtractor) {
        this.nodeparser = nodeparser;
        this.relationparser = relationparser;
        this.systemRelationsExtractor = systemRelationsExtractor;
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
