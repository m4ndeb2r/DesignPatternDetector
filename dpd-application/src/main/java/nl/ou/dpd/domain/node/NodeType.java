package nl.ou.dpd.domain.node;

/**
 * A representation of a node type. Sometimes a {@link Node} can have more than one {@link NodeType}. For example,
 * an interface has both type {@link #INTERFACE} and  type {@link #ABSTRACT_CLASS_OR_INTERFACE}.
 *
 * @author Martin de Boer
 */
public enum NodeType {
    CONCRETE_CLASS,
    ABSTRACT_CLASS,
    INTERFACE,
    ABSTRACT_CLASS_OR_INTERFACE,
    CLASS_WITH_PRIVATE_CONSTRUCTOR, // TODO: do we still want this? Reconsider this....
    DATATYPE
};
