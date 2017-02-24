
package nl.ou.dpd.fourtuples;

import java.util.ArrayList;
import java.util.SortedSet;

/**
 * @author E.M. van Doorn
 */

public class Solutions {

    private ArrayList<Solution> solutions;

    Solutions() {
        solutions = new ArrayList();
    }

    void add(Solution sol) {
        solutions.add(sol);
    }

    boolean isUniq(SortedSet names) {
        Solution sol = new Solution(names);

        boolean result = true;
        for (Solution s : solutions) {
            result = result && !s.isEqual(sol);
            if (!result) {
                return false;
            }
        }
        return result;
    }
}
