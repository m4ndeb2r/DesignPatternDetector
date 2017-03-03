package nl.ou.dpd.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Contains classes/interfaces of the "system under consideration" mapped to classes/interfaces of the design
 * pattern. (sys --> dp). Names o
 *
 * @author Martin de Boer
 */

public class MatchedClasses {

    private Map<Clazz, Clazz> classes;

    /**
     * TODO...
     */
    MatchedClasses() {
        classes = new HashMap<>();
    }

    /**
     * TODO...
     * @param nm
     */
    MatchedClasses(MatchedClasses classes) {
        this();

        Set<Clazz> sc = classes.getKeySet();
        for (Clazz c : sc) {
            add(c, classes.get(c));
        }
    }

    /**
     * @deprecated All show methods must go. There will be no printing to System.out soon.
     */
    void show(String name) {
        String value;

        if (!name.isEmpty()) {
            System.out.printf("Design Pattern: %s\n", name);
        }

        SortedSet<Clazz> sortedSet = getBoundedSortedKeySet();

        for (Clazz key : sortedSet) {
            System.out.printf("%20s --> %25s\n", key.getName(), classes.get(key).getName());
        }

        System.out.println("------------------------");
    }

    /**
     * TODO...
     * @param key
     * @return
     */
    boolean isEmpty(Clazz key) {
        return classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * TODO...
     * @param key
     * @param value
     * @return
     */
    boolean equals(Clazz key, Clazz value) {
        return get(key).equals(value);
    }

    /**
     * TODO...
     * @param key
     * @param value
     */
    void add(Clazz key, Clazz value) {
        classes.put(key, value);
    }

    /**
     * TODO...
     * @param key
     */
    void add(Clazz key) {
        add(key, Clazz.EMPTY_CLASS);
    }

    /**
     * TODO...
     * @param key
     * @return
     */
    Clazz get(Clazz key) {
        return classes.get(key);
    }

    /**
     * TODO...
     * @return
     */
    Set<Clazz> getKeySet() {
        return classes.keySet();
    }

    /**
     * TODO...
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
     * @param key
     * @return
     */
    boolean keyIsBounded(Clazz key) {
        return !classes.get(key).equals(Clazz.EMPTY_CLASS);
    }

    /**
     * TODO...
     * @param v
     * @return
     */
    boolean valueIsBounded(Clazz v) {
        final Collection<Clazz> verz = classes.values();

        for (Clazz dpc : verz) {
            if (dpc.equals(v)) {
                return true;
            }
        }

        return false;
    }
}
