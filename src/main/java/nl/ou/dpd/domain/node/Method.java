package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an method.
 *
 * @author Peter Vansweevelt
 */
public class Method {

    private final String id;
    private final String name;
    private Visibility visibility;
    private Set<Parameter> parameters;
    private Node returnType; //null if returnType is 'void'

    /**
     * Creates an method with the given id and name.
     *
     * @param name the name of the method
     * @param type the type of the method
     */
    public Method(String id, String name, Visibility visibility, Set<Parameter> parameters, Node returnType) {
        this.id = id;
        this.name = name;
        this.visibility = visibility;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    /**
     * Creates an method with the given id and name.
     *
     * @param name the name of the method
     * @param type the type of the method
     */
    public Method(String id, String name) {
        this.id = id;
        this.name = name;
        this.visibility = null;
     	this.parameters = new HashSet<Parameter>();
        this.returnType = null;    	
    }

    /**
	 * @return the parameters
	 */
	public Set<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Set<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
	}
	

	/**
	 * @return the returnType
	 */
	public Node getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(Node returnType) {
		this.returnType = returnType;
	}

	/**
     * Get the name of the method.
     *
     * @return the name of the method
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the method.
     *
     * @return the name of the method
     */
    public String getId() {
        return id;
    }

    /**
     * Get the visibility of the method.
     *
     * @return the visibility of the method as {@link Visibility}
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Set the visibility of the method.
     *
     * @param the visibility of the method as {@link Visibility}
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }


}