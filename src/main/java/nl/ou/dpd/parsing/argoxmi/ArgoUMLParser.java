package nl.ou.dpd.parsing.argoxmi;

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

    public ArgoUMLParser() {
        nodeparser = new ArgoUMLNodeParser();
        relationparser = new ArgoUMLRelationParser();
    }

    public SystemUnderConsideration parse(URL xmiUrl) {
        return this.parse(xmiUrl.getPath());
    }

    public SystemUnderConsideration parse(String xmiFilename) {
        final SystemUnderConsideration suc = relationparser.parse(xmiFilename, nodeparser.parse(xmiFilename));
        final SystemRelationsExtractor postparser = new SystemRelationsExtractor(suc);
        postparser.execute();
        return suc;
    }

}
