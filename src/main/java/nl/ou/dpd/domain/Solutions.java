package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * Indicates whether the specified {@link Solution} is not already present in this {@link Solutions}.
     *
     * @param sol the {@link Solution} to determine uniqueness for.
     * @return {@code true} if {@code sol} is not already present, or {@code false} otherwise.
     */
    public boolean isUniq(Solution sol) {
        for (Solution s : solutions) {
            if (s.isEqual(sol)) {
                return false;
            }
        }
        return true;
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
            final Set<Edge> superfluousEdges = solution.getSuperfluousEdges();

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

            // Note: we are not printing missing edges for backward compatibility (they were not printed in the original version)
        }
    }
}
