package nl.ou.dpd.fourtuples;

import java.util.ArrayList;
import java.util.Objects;
import java.util.SortedSet;

/**
 * @author E.M. van Doorn
 */
public class Solution {
    private ArrayList<String> solution;

    Solution(SortedSet<String> names) {
        solution = new ArrayList();
        solution.addAll(names);
    }

    boolean isEqual(Solution other) {
        return this.solution.equals(other.solution);
    }

}
