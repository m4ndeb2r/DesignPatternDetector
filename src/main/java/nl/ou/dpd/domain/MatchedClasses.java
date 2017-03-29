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

public final class MatchedClasses {

    // A Map to connect classes. Key: system class; value: design pattern class
    private Map<Clazz, Clazz> classes;

    /**
     * Constructs a new instance with no matched classes, initially.
     */
    MatchedClasses() {
        classes = new HashMap<>();
    }

    /**
     * Creates a duplicate of the specified {@code other}.
     *
     * @param other the object to duplicate.
     */
    MatchedClasses(final MatchedClasses other) {
        this();
        other.classes.keySet().forEach(c -> add(c, other.get(c)));
    }

    /**
     * Gets a value {@link Clazz} for the specified system {@link Clazz}. In other words: finds a design pattern class
     * that is matched to the specified system under consideration's class.
     *
     * @param systemClass the system {@link Clazz} to look for.
     * @return the matching design pattern {@link Clazz}.
     */
    public Clazz get(final Clazz systemClass) {
        return classes.get(systemClass);
    }

    /**
     * Filters this {@link MatchedClasses} and returns only the entries with the specified {@code systemClasses}.
     *
     * @param systemClasses the systemClasses to filter
     * @return a new instance of {@link MatchedClasses} containing the entries that were filtered.
     */
    MatchedClasses filter(final Set<Clazz> systemClasses) {
        MatchedClasses filtered = new MatchedClasses();
        systemClasses.forEach(key -> filtered.add(key, get(key)));
        return filtered;
    }

    /**
     * Returns all the system unders construction's classes that are bound to a design patter class, in a sorted order.
     *
     * @return a {@link SortedSet} of system under consideration {@link Clazz}'s
     */
    public SortedSet<Clazz> getBoundSystemClassesSorted() {
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
    boolean isSystemClassBound(final Clazz systemClass) {
        return !classes.get(systemClass).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * Returns whether the specified {@link Edge}s can be matched. Edges can be matched when one of the following rules
     * applies:
     * <ol>
     * <li>
     * the edge types must match (be equal or design pattern having INHERITANCE_MULTI and system under
     * consideration having INHERITANCE), and
     * </li><li>
     * the edges should have the same selfRef value, and
     * </li>
     * <li>
     * <ul>
     * <li>
     * both edges (system and design pattern) are unbound on both sides (no class is bound), or
     * </li>
     * <li>
     * one side of the system edge is already matched to one side of the design pattern edge, and the other sides of
     * both (system and design pattern edge are unbound), or
     * </li>
     * <li>
     * the system edge is alaready matched (both sides) to the design pattern edge
     * </li>
     * </ul>
     * </ol>
     *
     * @param systemEdge  the "system under consideration" edge
     * @param patternEdge the design pattern edge
     * @return {@code true} if a match is possible (or already made), or {@code false} otherwise.
     */
    boolean canMatch(final Edge systemEdge, final Edge patternEdge) {

        return areEdgeTypesCompatible(systemEdge, patternEdge)
                && patternEdge.isSelfRef() == systemEdge.isSelfRef()
                && (hasAllClassesUnbound(systemEdge, patternEdge)
                || hasLeftClassBound(systemEdge, patternEdge)
                || hasRightClassBound(systemEdge, patternEdge)
                || hasBothClassesMatched(systemEdge, patternEdge)
        );
    }

    private boolean hasBothClassesMatched(final Edge systemEdge, final Edge patternEdge) {
        return this.isMatched(systemEdge.getClass1(), patternEdge.getClass1())
                && this.isMatched(systemEdge.getClass2(), patternEdge.getClass2());
    }

    private boolean hasRightClassBound(final Edge systemEdge, final Edge patternEdge) {
        return this.isUnbound(systemEdge.getClass1())
                && !this.designPatternClassIsBound(patternEdge.getClass1())
                && this.isMatched(systemEdge.getClass2(), patternEdge.getClass2());
    }

    private boolean hasLeftClassBound(final Edge systemEdge, final Edge patternEdge) {
        return this.isMatched(systemEdge.getClass1(), patternEdge.getClass1())
                && this.isUnbound(systemEdge.getClass2())
                && !this.designPatternClassIsBound(patternEdge.getClass2());
    }

    private boolean hasAllClassesUnbound(final Edge systemEdge, final Edge patternEdge) {
        return this.isUnbound(systemEdge.getClass1())
                && this.isUnbound(systemEdge.getClass2())
                && !this.designPatternClassIsBound(patternEdge.getClass1())
                && !this.designPatternClassIsBound(patternEdge.getClass2());
    }

    private boolean areEdgeTypesCompatible(final Edge sysEdge, final Edge dpEdge) {
        return dpEdge.getRelationType() == sysEdge.getRelationType() || isInheritanceMultiMatch(sysEdge, dpEdge);
    }

    private boolean isInheritanceMultiMatch(final Edge sysEdge, final Edge dpEdge) {
        return dpEdge.getRelationType() == EdgeType.INHERITANCE_MULTI
                && sysEdge.getRelationType() == EdgeType.INHERITANCE;
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
    void makeMatch(final Edge systemEdge, final Edge patternEdge) {
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
    void prepareMatch(final Edge systemEdge) {
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
    private boolean isUnbound(final Clazz key) {
        return classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * Determines whether two classes are already matched.
     *
     * @param key   the {@link Clazz} to lookup and find a mathed {@link Clazz} for.
     * @param value the {@link Clazz} we hope to find
     * @return {@code true} is a match was found, or {@code false} otherwise.
     */
    private boolean isMatched(final Clazz key, final Clazz value) {
        return get(key).equals(value);
    }

    /**
     * Detemines whether the specified design pattern {@link Clazz} is already matched with some "system under
     * consideration" {@link Clazz}.
     *
     * @param designPatternClass the design pattern class to check for being bound.
     * @return {@code true} if the specified {@code designPatternClass} is already bound, or {@code false} otherwise.
     */
    private boolean designPatternClassIsBound(final Clazz designPatternClass) {
        for (Clazz dpClass : classes.values()) {
            if (dpClass.equals(designPatternClass)) {
                return true;
            }
        }
        return false;
    }

    private void add(final Clazz key, final Clazz value) {
        classes.put(key, value);
    }

    private void add(final Clazz key) {
        add(key, Clazz.EMPTY_CLASS);
    }

}
