package nl.ou.dpd.fourtuples;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Temporarily used for refactoring en testing the order method of {@link FourTupleArray}.
 * TODO: remove this test after it's become obsolete
 *
 * @author Martin de Boer
 */
public class FourTuplesOrderTest {

    private List<FourTuple> fourTuples;
    private List<FourTuple> fourTuplesNew;

    @Before
    public void setUp() {
        fourTuples = new ArrayList<>();
        fourTuples.add(new FourTuple("A", "B", 1));
        fourTuples.add(new FourTuple("C", "D", 1));
        fourTuples.add(new FourTuple("E", "F", 1));
        fourTuples.add(new FourTuple("A", "C", 1));
        fourTuples.add(new FourTuple("A", "E", 1));

        fourTuplesNew = new ArrayList<>();
        fourTuples.forEach(ft -> fourTuplesNew.add(new FourTuple(ft.getClassName1(), ft.getClassName2(), ft.getTypeRelation())));
    }

    @Test
    public void testOldAndNewOrder() {
        System.out.println("Old before order():");
        for (int i = 0; i < fourTuples.size(); i++) {
            System.out.println(fourTuples.get(i).getClassName1() + "->" + fourTuples.get(i).getClassName2());
        }
        System.out.println("\nNew before order():");
        for (int i = 0; i < fourTuplesNew.size(); i++) {
            System.out.println(fourTuplesNew.get(i).getClassName1() + "->" + fourTuplesNew.get(i).getClassName2());
        }


        // Old way
        order();
        boolean isEqual = true;
        for (int i = 0; i < fourTuples.size(); i++) {
            isEqual = isEqual && (fourTuples.get(i).getClassName1().equals(fourTuplesNew.get(i).getClassName1()));
        }
        assertThat(isEqual, is(false));

        // New way
        fourTuplesNew = orderNew(fourTuplesNew);
        for (int i = 0; i < fourTuples.size(); i++) {
            assertThat(fourTuples.get(i).getClassName1(), is(fourTuplesNew.get(i).getClassName1()));
            assertThat(fourTuples.get(i).getClassName2(), is(fourTuplesNew.get(i).getClassName2()));
        }


        System.out.println("\nOld after order():");
        for (int i = 0; i < fourTuples.size(); i++) {
            System.out.println(fourTuples.get(i).getClassName1() + "->" + fourTuples.get(i).getClassName2());
        }
        System.out.println("\nNew after order():");
        for (int i = 0; i < fourTuplesNew.size(); i++) {
            System.out.println(fourTuplesNew.get(i).getClassName1() + "->" + fourTuplesNew.get(i).getClassName2());
        }
    }


    /**
     * This method orders the array of {@link FourTuple}'s. It guarantees that every edge in the graph has at least one
     * vertex that is also present in one or more preceding edges. One exception to this rule is the first edge in the
     * graph, obviously because it has no preceding edge. In other words: for every edge E(v1 -> v2) in the graph
     * (except the first one), a previous edge E(v1 -> x2) or E(x1 -> v2) is present. This way every edge is connected
     * to a vertex of a preceding edge.
     * <p/>
     * Example: A->B, C->D, A->C becomes A->B, A->C, C->D.
     *
     * @param graph the graph to order
     * @return the ordered graph
     */
    private List<FourTuple> orderNew(List<FourTuple> graph) {
        // Skip the first element. It stays where it is. Start with i = 1.
        for (int i = 1; i < graph.size(); i++) {

            // The i-element of fourTuple should have a classname that occurs in the elements 0.. (i-1)
            boolean found = false;
            for (int j = i; j < graph.size() && !found; j++) {

                for (int k = 0; k < i && !found; k++) {
                    found = areConnected(graph.get(j), graph.get(k));
                    if (found && (j != i)) {
                        // Switch elements
                        final FourTuple temp = graph.get(j);
                        graph.set(j, graph.get(i));
                        graph.set(i, temp);
                    }
                }
            }
            if (!found) {
                System.out.println("Warning: Template is not a connected graph.");
            }
        }
        return graph;
    }

    private boolean areConnected(FourTuple edge1, FourTuple edge2) {
        final String v1 = edge1.getClassName1();
        final String v2 = edge1.getClassName2();
        final String v3 = edge2.getClassName1();
        final String v4 = edge2.getClassName2();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }


    private void order() {
        // Zorgt er voor de volgorde van de edges A -> B, C->D, A->C
        // wordt A->B, A->C, C->D
        // In het algemeen: vanaf de tweede edges moet de kant een knoop bevatten
        // die reeds is voorgekomen.

        boolean found;
        String name1, name2, name3, name4;
        FourTuple ft;

        for (int i = 1; i < fourTuples.size(); i++) {
            // The i-element of fourTuple should have a classname which occurs
            // in the elements 0.. (i-1)

            found = false;
            for (int j = i; j < fourTuples.size() && !found; j++) {
                // Does element j satifies these condition ?
                // yes --> found = true

                name1 = fourTuples.get(j).getClassName1();
                name2 = fourTuples.get(j).getClassName2();

                for (int k = 0; k < i && !found; k++) {
                    name3 = fourTuples.get(k).getClassName1();
                    name4 = fourTuples.get(k).getClassName2();

                    if (name1.equals(name3) || name1.equals(name4) ||
                            name2.equals(name3) || name2.equals(name4)) {
                        found = true;

                        if (j != i) {
                            ft = fourTuples.get(j);
                            fourTuples.set(j, fourTuples.get(i));
                            fourTuples.set(i, ft);
                        }
                    }
                }
            }

            if (!found)
                System.out.println("Warning: Template is not a connected graph.");
        }
    }

}
