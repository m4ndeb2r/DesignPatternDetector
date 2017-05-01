package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edges;

/**
 * Represents a system design that is under consideration. Under consideration meaning that it is being analysed for
 * the presence of any known/detectable design pattern.
 *
 * @author Martin de Boer
 */
public class SystemUnderConsideration extends Edges {
	
	private String id;
	private String name;

	public SystemUnderConsideration() {
	}

	
	public SystemUnderConsideration(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}
