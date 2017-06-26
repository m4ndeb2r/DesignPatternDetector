package nl.ou.dpd.domain.node;

public enum NodeType {
    CONCRETE_CLASS,
    ABSTRACT_CLASS,
    INTERFACE,
    CLASS_WITH_PRIVATE_CONSTRUCTOR, // TODO: do we still want this? Reconsider this....
    DATATYPE
};
