package nl.ou.dpd.fourtuples;

import java.util.List;

/**
 * This class represents a pattern detector that analyses a "system under consideration" (an application design) and
 * detects any design pattern present in it.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class DetectPatterns {

    /**
     * Detects design patters in a "system under consideration".
     *
     * @param fta             a {@link FourTupleArray}, representing the "system under consideration". This represents
     *                        the system (application design) that is being analysed.
     * @param dps             a list of {@link FourTupleArray}s representing the design patterns to look for in the
     *                        application design, represented by the {@code fta} argument.
     * @param maxMissingEdges the maximum number of allowed missing edges. Edges represent the relations between
     *                        classes.
     */
    public void detectDP(final FourTupleArray fta, final List<FourTupleArray> dps, int maxMissingEdges) {

        // Find a match for each design pattern in dsp
        dps.forEach(dp -> dp.match(fta, maxMissingEdges));

        // TODO: return something that is testable in a unittest, instead of printing everything to stdout....
    }
}
