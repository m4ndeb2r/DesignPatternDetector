package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link Solution}s.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solutions {

    private final List<Solution> solutions;

    /**
     * Default constructor. Constructs a {@link Solution}s instance containing an empty list of {@link Solution}s.
     */
    Solutions() {
        solutions = new ArrayList();
    }

    /**
     * Adds a {@link Solution} to the list.
     *
     * @param sol the {@link Solution} to add to the list
     */
    public void add(Solution sol) {
        solutions.add(sol);
    }

    /**
     * TODO....
     *
     * @param sol
     * @return
     */
    public boolean isUniq(Solution sol) {
        boolean result = true;
        for (Solution s : solutions) {
            result = result && !s.isEqual(sol);
            if (!result) {
                return false;
            }
        }
        return result;
    }

    /**
     * Returns the list of {@link Solution}s.
     *
     * @return the list of  {@link Solution}s.
     */
    public List<Solution> getSolutions() {
        return solutions;
    }

    /**
     * This method is still available for backwards compatibility purposes. The original version of the application
     * printed all the results to the console. In the next version we will not do that anymore. Instead a GUI is being
     * considered to show feedback to the user.
     *
     * @deprecated no output to the console directly in this application!!
     */
    public void show() {
        for (Solution solution : solutions) {
            final String designPatternName = solution.getDesignPatternName();
            final MatchedClasses matchedClasses = solution.getMatchedClasses();
            final List<Edge> superfluousEdges = solution.getSuperfluousEdges();

            final StringBuffer output = new StringBuffer();

            if (!designPatternName.isEmpty()) {
                output.append("Design Pattern: ").append(designPatternName).append("\n");
            }
            for (Clazz key : matchedClasses.getBoundedSortedKeySet()) {
                output.append(String.format("%20s --> %25s\n", key.getName(), matchedClasses.get(key).getName()));
            }
            output.append("------------------------\n");

            if (superfluousEdges.size() > 0) {
                output.append("Edges which do not belong to this design pattern:\n");
                for (Edge edge : superfluousEdges) {
                    if (!edge.isVirtual()) {
                        output.append(edge.getClass1().getName())
                                .append(" --> ")
                                .append(edge.getClass2().getName())
                                .append("\n");
                    }
                }
                output.append("==================================================\n");
            }
            System.out.println(output.toString());
        }
    }
}
