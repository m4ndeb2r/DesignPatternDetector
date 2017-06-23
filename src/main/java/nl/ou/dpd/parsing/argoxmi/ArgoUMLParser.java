package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;

import java.net.URL;

/**
 * @author Peter Vansweevelt
 */
public class ArgoUMLParser {

    private SystemUnderConsideration suc;
    private final ArgoUMLNodeParser nodeparser;
    private final ArgoUMLRelationParser relationparser;

    public ArgoUMLParser() {
        nodeparser = new ArgoUMLNodeParser();
        relationparser = new ArgoUMLRelationParser();
    }

    public SystemUnderConsideration parse(URL xmiUrl) {
        return this.parse(xmiUrl.getPath());
    }

    public SystemUnderConsideration parse(String filename) {
        suc = relationparser.parse(filename, nodeparser.parse(filename));
        SystemRelationsExtractor postparser = new SystemRelationsExtractor(suc);
        postparser.execute();
        return suc;
    }

}
