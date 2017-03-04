package nl.ou.dpd.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Contains classes/interfaces of the "system under consideration" mapped to classes/interfaces of the design
 * pattern. (sys --> dp).
 *
 * @author Martin de Boer
 */

public class MatchedClasses {

    private Map<Clazz, Clazz> classes;

    /**
     * Constructs a new instance with no matched classes, initially.
     */
    MatchedClasses() {
        classes = new HashMap<>();
    }

    /**
     * Creates a duplicate of the specified {@code classes}.
     *
     * @param classes the object to duplicate.
     */
    MatchedClasses(MatchedClasses classes) {
        this();
        for (Clazz c : classes.getKeySet()) {
            add(c, classes.get(c));
        }
    }

    /**
     * Determines whether a {@link Clazz}, specified by {@code key}, is matched with an empty class (or, in other words,
     * is not yet connected to another {@link Clazz}.
     *
     * @param key the {@link Clazz} to lookup and find a mathed {@link Clazz} for.
     * @return {@code true} when {@code key}s matched {@link Clazz} is {@link Clazz#EMPTY_CLASS}.
     */
    boolean isEmpty(Clazz key) {
        return classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * TODO...
     *
     * @param key
     * @param value
     * @return
     */
    boolean equals(Clazz key, Clazz value) {
        return get(key).equals(value);
    }

    /**
     * TODO...
     *
     * @param key
     * @return
     */
    Clazz get(Clazz key) {
        return classes.get(key);
    }

    /**
     * TODO...
     *
     * @param key
     * @return
     */
    MatchedClasses filter(Set<Clazz> keys) {
        MatchedClasses filtered = new MatchedClasses();
        keys.forEach(key -> filtered.add(key, get(key)));
        return filtered;
    }

    /**
     * TODO...
     *
     * @return
     */
    Set<Clazz> getKeySet() {
        return classes.keySet();
    }

    /**
     * TODO...
     *
     * @return
     */
    SortedSet<Clazz> getBoundedSortedKeySet() {
        return new TreeSet(getKeySet()
                .stream()
                .filter(key -> keyIsBounded(key))
                .collect(Collectors.toSet()));
    }

    /**
     * TODO...
     *
     * @param key
     * @return
     */
    boolean keyIsBounded(Clazz key) {
        return !classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * TODO...
     *
     * @param v
     * @return
     */
    boolean valueIsBounded(Clazz v) {

        for (Clazz dpc : classes.values()) {
            if (dpc.equals(v)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the specified {@link Edge}s can be matched.
     * TODO: explain this is bit more extensively
     *
     * @param systemEdge  the "system under consideration" edge
     * @param patternEdge the design pattern edge
     * @return {@code true} if a match can be made, or {@code false} otherwise.
     */
    boolean canMatch(Edge systemEdge, Edge patternEdge) {
        if (patternEdge.getTypeRelation() != systemEdge.getTypeRelation()) {
            if (patternEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI
                    && systemEdge.getTypeRelation() == EdgeType.INHERITANCE)
                ; // break; generates a warning.
            else {
                return false;
            }
        }

        if (patternEdge.getSelfRef() != systemEdge.getSelfRef()) {
            return false;
        }

        // two empty names
        if (this.isEmpty(systemEdge.getClass1())
                && this.isEmpty(systemEdge.getClass2())
                && !this.valueIsBounded(patternEdge.getClass1())
                && !this.valueIsBounded(patternEdge.getClass2())) {
            return true;
        }

        // first name matched, second name empty
        if (this.equals(systemEdge.getClass1(), patternEdge.getClass1())
                && this.isEmpty(systemEdge.getClass2())
                && !this.valueIsBounded(patternEdge.getClass2())) {
            return true;
        }

        // first name empty, second name matched
        if (this.isEmpty(systemEdge.getClass1())
                && !this.valueIsBounded(patternEdge.getClass1())
                && this.equals(systemEdge.getClass2(), patternEdge.getClass2())) {
            return true;
        }

        // both names are already matched.
        if (this.equals(systemEdge.getClass1(), patternEdge.getClass1())
                && this.equals(systemEdge.getClass2(), patternEdge.getClass2())) {
            return true;
        }

        return false;
    }

    /**
     * Marks two {@link Edge}s as matched. This happens when a design pattern is detected in the "system under
     * consideration", and must be carried out for all the edges in the pattern and all the involved edges in the
     * "system under consideration". Both edges are also locked to prevent them form being matched again (no bigamy
     * allowed here ...)
     *
     * @param systemEdge  the edge for the "system under consideration" to match.
     * @param patternEdge the edge from the design pattern to match.
     */
    void makeMatch(Edge systemEdge, Edge patternEdge) {
        add(systemEdge.getClass1(), patternEdge.getClass1());
        add(systemEdge.getClass2(), patternEdge.getClass2());
        systemEdge.lock();
        patternEdge.lock();
    }

    /**
     * Makes an "empty" match. It merely reserve space for the specified {@link Edge} to be matched later.
     *
     * @param systemEdge the edge to prepare space for.
     */
    void prepareMatch(Edge systemEdge) {
        add(systemEdge.getClass1());
        add(systemEdge.getClass2());
    }

    private void add(Clazz key, Clazz value) {
        classes.put(key, value);
    }

    private void add(Clazz key) {
        add(key, Clazz.EMPTY_CLASS);
    }

}
