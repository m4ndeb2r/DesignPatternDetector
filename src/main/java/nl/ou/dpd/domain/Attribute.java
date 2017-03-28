/**
 * 
 */
package nl.ou.dpd.domain;

/**
 * Represents an attribute of a {@link Clazz}.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Attribute {
	
	private final String name;
	private final Clazz type;
	
	/**
     * Creates an attribute with the given name and {@link Clazz} type.
     *
     * @param name the name of the attribute;
     * @param type the type of the attribute
     */	
	public Attribute(String name, Clazz type) {
		this.name = name;
		this.type = type;		
	}
	
	/**
	 * Get the name of the attribute.
	 * @return the name of the attribute
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the type of the attribute.
	 * @return the type of the attribute as {@link Clazz}
	 */
	public Clazz getType() {
		return type;
	}

}
