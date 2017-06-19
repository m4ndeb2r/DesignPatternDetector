package nl.ou.dpd.parsing.argoxmi;

import java.net.URL;

import nl.ou.dpd.domain.SystemUnderConsideration;

/**
 * @author Peter Vansweevelt
 *
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
		String filename = xmiUrl.getPath();
		suc = relationparser.parse(filename, nodeparser.parse(filename));
		SystemRelationsExtractor postparser = new SystemRelationsExtractor(suc);
		postparser.execute();
		return suc;
	}

}
