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

    // A Map to connect classes. Key: system class; value: design pattern class
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
        for (Clazz c : classes.classes.keySet()) {
            add(c, classes.get(c));
        }
    }

    /**
     * Gets a value {@link Clazz} for the specified system {@link Clazz}. In other words: finds a design pattern class
     * that is matched to the specified system under consideration's class.
     *
     * @param systemClass the system {@link Clazz} to look for.
     * @return the matching design pattern {@link Clazz}.
     */
    Clazz get(Clazz systemClass) {
        return classes.get(systemClass);
    }

    /**
     * Filters this {@link MatchedClasses} and returns only the entries with the specified {@code systemClasses}.
     *
     * @param systemClasses the systemClasses to filter
     * @return a new instance of {@link MatchedClasses} containing the entries that were filtered.
     */
    MatchedClasses filter(Set<Clazz> systemClasses) {
        MatchedClasses filtered = new MatchedClasses();
        systemClasses.forEach(key -> filtered.add(key, get(key)));
        return filtered;
    }

    /**
     * Returns all the system unders construction's classes that are bound to a design patter class, in a sorted order.
     *
     * @return a {@link SortedSet} of system under consideration {@link Clazz}'s
     */
    SortedSet<Clazz> getBoundSystemClassesSorted() {
        return new TreeSet(classes.keySet()
                .stream()
                .filter(key -> isSystemClassBound(key))
                .collect(Collectors.toSet()));
    }

    /**
     * Returns whether the system under construction's class is bound to a design pattern class.
     *
     * @param systemClass the system under construction's class to check
     * @return {@code true} is {@code systemClass} is bound to a design pattern class.
     */
    boolean isSystemClassBound(Clazz systemClass) {
        return !classes.get(systemClass).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * Returns whether the specified {@link Edge}s can be matched. Edges can be matched when the following rules apply:
     * <ol>
     * <li>
     * the edge types must match (be equal or design pattern having INHERITANCE_MULTI and system under
     * consideration having INHERITANCE
     * </li><li>
     * the edges should have the same selfRef value
     * </li>
     * <li>
     *
     * </li>
     * </ol>
     * TODO: explain this is bit more extensively
     *
     * @param systemEdge  the "system under consideration" edge
     * @param patternEdge the design pattern edge
     * @return {@code true} if a match can be made, or {@code false} otherwise.
     */
    boolean canMatch(Edge systemEdge, Edge patternEdge) {

        // Edgetypes should be compatible
        if (patternEdge.getTypeRelation() != systemEdge.getTypeRelation()) {
            if (patternEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI
                    && systemEdge.getTypeRelation() == EdgeType.INHERITANCE)
                ; // break; generates a warning.
            else {
                return false;
            }
        }

        // SelfRef should be compatible (equal)
        if (patternEdge.isSelfRef() != systemEdge.isSelfRef()) {
            return false;
        }

        final Clazz systemClass1 = systemEdge.getClass1();
        final Clazz systemClass2 = systemEdge.getClass2();
        final Clazz designPatternClass1 = patternEdge.getClass1();
        final Clazz designPatternClass2 = patternEdge.getClass2();

        // two empty names
        if (this.isUnbound(systemClass1)
                && this.isUnbound(systemClass2)
                && !this.designPatternClassIsBound(designPatternClass1)
                && !this.designPatternClassIsBound(designPatternClass2)) {
            return true;
        }

        // first name matched, second name empty
        if (this.isMatched(systemClass1, designPatternClass1)
                && this.isUnbound(systemClass2)
                && !this.designPatternClassIsBound(designPatternClass2)) {
            return true;
        }

        // first name empty, second name matched
        if (this.isUnbound(systemClass1)
                && !this.designPatternClassIsBound(designPatternClass1)
                && this.isMatched(systemClass2, designPatternClass2)) {
            return true;
        }

        // both names are already matched.
        if (this.isMatched(systemClass1, designPatternClass1)
                && this.isMatched(systemClass2, designPatternClass2)) {
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

    /**
     * Determines whether a {@link Clazz}, specified by {@code key}, is matched with an empty class (or, in other words,
     * is not yet connected to another {@link Clazz}.
     *
     * @param key the {@link Clazz} to lookup and find a mathed {@link Clazz} for.
     * @return {@code true} when {@code key}s matched {@link Clazz} is {@link Clazz#EMPTY_CLASS}.
     */
    private boolean isUnbound(Clazz key) {
        return classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * Determines whether two classes are already matched.
     *
     * @param key   the {@link Clazz} to lookup and find a mathed {@link Clazz} for.
     * @param value the {@link Clazz} we hope to find
     * @return {@code true} is a match was found, or {@code false} otherwise.
     */
    private boolean isMatched(Clazz key, Clazz value) {
        return get(key).equals(value);
    }

    /**
     * Detemines whether the specified design pattern {@link Clazz} is already matched with some "system under
     * consideration" {@link Clazz}.
     *
     * @param designPatternClass the design pattern class to check for being bound.
     * @return {@code true} if the specified {@code designPatternClass} is already bound, or {@code false} otherwise.
     */
    private boolean designPatternClassIsBound(Clazz designPatternClass) {
        for (Clazz dpClass : classes.values()) {
            if (dpClass.equals(designPatternClass)) {
                return true;
            }
        }
        return false;
    }

    private void add(Clazz key, Clazz value) {
        classes.put(key, value);
    }

    private void add(Clazz key) {
        add(key, Clazz.EMPTY_CLASS);
    }

}
