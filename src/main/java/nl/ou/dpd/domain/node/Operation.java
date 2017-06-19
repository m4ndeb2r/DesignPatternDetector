package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an method.
 *
 * @author Peter Vansweevelt
 */
public class Operation implements SignatureComparable {

 	private final String id;
    private final Node parentNode;
    private String name;
    private Visibility visibility;
    private Set<Parameter> parameters;
    private Node returnType; //null if returnType is 'void'

    /**
     * Creates an method with the given id and name.
     *
     * @param name the name of the method
     * @param type the type of the method
     */
/*    public Operation(String id, String name, Visibility visibility, Set<Parameter> parameters, Node returnType) {
        this.id = id;
        this.name = name;
        this.visibility = visibility;
        this.parameters = parameters;
        this.returnType = returnType;
    }
*/
    /**
     * Creates an method with the given id and name,default Visibility public and default return type null (void).
     *
     * @param name the name of the method
     * @param type the type of the method
     */
    public Operation(String id, Node parentNode) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = null;
        this.visibility = Visibility.PUBLIC;
     	this.parameters = new HashSet<Parameter>();
        this.returnType = null;    	
        if (parentNode != null) {
        	updateParentNode();
        }
   }

    /**
	 * set the operation in the parentNode
	 */
	private void updateParentNode() {
		parentNode.addOperation(this);		
	}

	   /**
	 * @return the parentNode
	 */
	public Node getParentNode() {
		return parentNode;
	}

	/**
	 * @return the parameters
	 */
	public Set<Parameter> getParameters() {
		return parameters;
	}
	
	protected void addParameter(Parameter parameter) {
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

    public void setName(String name) {
        this.name = name;
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
 
    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		return result;
	}

 	/* 
	 * Looks if the signature of this Operation matches the signature of another Operation.
	 * This means that name, returntype and attributes are the same.
 	 */
 	public boolean equalsSignature(Object obj) {
 		if (this == obj)
 			return true;
 		if (obj == null)
 			return false;
 		if (getClass() != obj.getClass())
 			return false;
 		Operation other = (Operation) obj;
 		if (name == null) {
 			if (other.name != null)
 				return false;
 		} else if (!name.equals(other.name))
 			return false;
 		if (!equalsSignatures(parameters, other.parameters))
 			return false;
 		if (returnType == null) {
 			if (other.returnType != null)
 				return false;
 		} else if (!returnType.equalsSignature(other.returnType))
 			return false;
 		return true;
 	}

	/**
	 * @param sourceParameters
	 * @param targetParameters
	 * @return
	 */
	private boolean equalsSignatures(Set<Parameter> sourceParameters, Set<Parameter> targetParameters) {
		if (sourceParameters.size() == 0 && targetParameters.size() == 0) {
			return true;
		}
		if (sourceParameters.size() != targetParameters.size()) {
			return false;
		}
		Boolean allTrue = true;
		for (Parameter sourceParameter : sourceParameters) {
			Boolean oneTrue = false;
			for (Parameter targetParameter : targetParameters) {
				if (sourceParameter.equalsSignature(targetParameter)) {
					oneTrue = true;
				}
			}
			allTrue = allTrue && oneTrue;
		}
		return allTrue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operation other = (Operation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!(parameters.hashCode() == other.parameters.hashCode()))
			return false;
		if (parentNode == null) {
			if (other.parentNode != null)
				return false;
		} else if (!parentNode.equals(other.parentNode))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}



}