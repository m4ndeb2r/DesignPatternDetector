package nl.ou.dpd.domain;

/**
 * TODO ...
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdgeCreator implements EdgeCreator<SystemUnderConsiderationEdge> {

    /**
     * TODO ...
     *
     * @param class1
     * @param class2
     * @param type
     * @return
     */
    public SystemUnderConsiderationEdge create(String class1, String class2, EdgeType type) {
        return new SystemUnderConsiderationEdge(class1, class2, type);
    }

    /**
     * TODO ...
     *
     * @param edge
     * @return
     */
    public SystemUnderConsiderationEdge create(SystemUnderConsiderationEdge edge) {
        return new SystemUnderConsiderationEdge(edge);
    }

}
