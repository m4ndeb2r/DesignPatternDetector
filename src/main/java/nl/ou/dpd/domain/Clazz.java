package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents a class or interface in a design pattern or a "system under consideration". A {@link Clazz} is typically
 * connected to another {@link Clazz} within the same design pattern or "system under consideration". Two connected
 * {@link Clazz}'s form an {@link Edge}. A combination of {@link Edge}s (combined in a subclass of {@link Edges}) form a
 * design pattern or a system design. {@link Edge}s can have an {@link EdgeType}, defining the type of relation between
 * the {@link Clazz}'s.
 * <p>
 * A {@link Clazz} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public class Clazz implements Comparable<Clazz> {

    /**
     * An "empty" {@link Clazz}. The {@link #EMPTY_CLASS} is used to prepare a "match" (similarity) between a class or
     * interface in a pattern and the system design. It is a placeholder for a matching {@link Clazz} that is not yet
     * identified.
     */
    public static final Clazz EMPTY_CLASS = new Clazz("");
    
    public enum ClassType {CLASS, INTERFACE};
    public enum Visibility {PUBLIC, PROTECTED, PACKAGE, PRIVATE};
    
    private final String id;
    private final String name;
    private ClassType classType;

    private List<Attribute> attributes;
    private HashMap<ClazzConstant, Boolean> modifiers;
    private Visibility visibility;
    
    /**
     * Constructs a {@link Class} instance with the specified {@code name}.
     *
     * @param name the classname of this {@link Clazz}
     */
    public Clazz(final String name) {
        this(name, name);        
   }

    /**
     * Constructs a {@link Class} instance with the specified {@code id} and {@code name}.
     * Initializes the attributes type, attributes, modifiers and visibility.
     *
     * @param id the id of this {@link Clazz}
     * @param name the classname of this {@link Clazz}
     */
    public Clazz(final String id, final String name) {
        this.id = id;
        this.name = name;
        this.classType = null;
        this.attributes = new ArrayList<Attribute>();
        this.modifiers = initializeModifiers();
        this.visibility = null;        
    }

    /**
     * Creates a HashMap to keep track of the modifiers of the {@Clazz} and initializes it setting all values to <code>false</code>.
	 * @return the initialized HashMap
	 */
	private HashMap<ClazzConstant, Boolean> initializeModifiers() {
		HashMap<ClazzConstant, Boolean> temp = new HashMap<ClazzConstant, Boolean>();
		temp.put(ClazzConstant.MODIFIER_ISABSTRACT, null);
		temp.put(ClazzConstant.MODIFIER_ISACTIVE, null);
		temp.put(ClazzConstant.MODIFIER_ISLEAF, null);
		temp.put(ClazzConstant.MODIFIER_ISROOT, null);
		return temp;
	}

	/**
     * Gets the name of the class.
     *
     * @return the classname
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the class.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the type of the class.
     *
     * @return the classtype
     */
    public ClassType getClassType() {
        return classType;
    }

    /**
     * Sets the type of the class.
     *
     * @param the classtype
     */
    public void setClassType(ClassType type) {
        this.classType =  type;
    }
    
    /**
     * Gets the visibility of the class.
     *
     * @return the visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibiliy of the class.
     *
     * @param the visibility
     */
    public void setVisibility(Visibility visibility) {
        this.visibility =  visibility;
    }

    /**
     * Gets the modifiers.
     *
     * @return the HashMap of the modifiers.
     */
    public HashMap<ClazzConstant, Boolean> getModifiers() {
        return modifiers;
    }

    
    /**
     * Gets the status of the modifier.
     *
     * @return <code>True</code> if the modifier is set. <code> False</code> otherwise.
     */
    public Boolean getModifierStatus(ClazzConstant modifier) {
        return modifiers.get(modifier);
    }

    /**
     * Sets the status of a modifier.
     *
     * @param modifier the modifier
     * @param status the new status
     * @return the new status
     */
    public boolean setModifier(ClazzConstant modifier, boolean status) {
    	modifiers.put(modifier, status);
        return status;
    }  
    
    /**
     * Sets an attribute of the class.
     *
     * @param the attribute
     */
    public void setAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }
    
    /**
     * Gets the attributes of the class.
     *
     * @return the attributes of the class
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets the attributes with the given name.
     *
     * @return the attributes of the class which names equal the given name
     */
    public ArrayList<Attribute> getAttributesByName(String attributeName) {
    	ArrayList<Attribute> temp = new ArrayList<Attribute>();
    	for (Attribute attr : attributes) {
    		if (attr.getName().equals(attributeName)) {
    			temp.add(attr);
    		}
    	}    	
        return temp;
    }

    /**
     * Gets the attributes with the given type.
     *
     * @return the attributes of the class which names equal the given type
     */
    public ArrayList<Attribute> getAttributesByType(Clazz attributeType) {
    	ArrayList<Attribute> temp = new ArrayList<Attribute>();
    	for (Attribute attr : attributes) {
    		if (attr.getType().equals(attributeType)) {
    			temp.add(attr);
    		}
    	}    	
        return temp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(name, clazz.name)
                && Objects.equals(id, clazz.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Clazz other) {
        if (other == null) {
            return 1;
        }
        final int compareByName = compareByName(other);
        if (compareByName == 0) {
            return compareById(other);
        }
        return compareByName;
    }

    private int compareByName(final Clazz other) {
        if (getName() == null && other.getName() != null) {
            return -1;
        }
        if (getName() != null && other.getName() == null) {
            return 1;
        }
        if (getName() == null && other.getName() == null) {
            return 0;
        }
        return this.getName().compareTo(other.getName());
    }

    private int compareById(final Clazz other) {
        if (getId() == null && other.getId() != null) {
            return -1;
        }
        if (getId() != null && other.getId() == null) {
            return 1;
        }
        if (getId() == null && other.getId() == null) {
            return 0;
        }
        return this.getId().compareTo(other.getId());
    }

}
