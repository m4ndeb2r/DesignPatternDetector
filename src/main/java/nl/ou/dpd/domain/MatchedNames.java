package nl.ou.dpd.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Contains class/interface names of the "system under consideration" mapped to class/interface names of the design
 * pattern. (sys-names --> dp-names).
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

public class MatchedNames {

    private Map<String, String> names;

    /**
     * TODO...
     */
    MatchedNames() {
        names = new HashMap();
    }

    /**
     * TODO...
     * @param nm
     */
    MatchedNames(MatchedNames nm) {
        this();

        Set<String> s = nm.getKeySet();
        for (String name : s) {
            add(name, nm.get(name));
        }
    }

    /**
     * @deprecated All show methods must go. There will be no printing to System.out soon.
     */
    void show(String name) {
        String value;

        if (!name.equals(EdgeType.EMPTY.getName())) {
            System.out.printf("Design Pattern: %s\n", name);
        }

        SortedSet<String> sortedSet = getBoundedSortedKeySet();

        for (String key : sortedSet) {
            System.out.printf("%20s --> %25s\n", key, names.get(key));
        }

        System.out.println("------------------------");
    }

    /**
     * TODO...
     * @param key
     * @return
     */
    boolean isEmpty(String key) {
        return names.get(key).equals(EdgeType.EMPTY.getName());
    }

    /**
     * TODO...
     * @param key
     * @param value
     * @return
     */
    boolean equals(String key, String value) {
        return get(key).equals(value);
    }

    /**
     * TODO...
     * @param key
     * @param value
     */
    void add(String key, String value) {
        names.<String, String>put(key, value);
    }

    /**
     * TODO...
     * @param key
     */
    void add(String key) {
        add(key, EdgeType.EMPTY.getName());
    }

    /**
     * TODO...
     * @param key
     * @return
     */
    String get(String key) {
        return names.<String>get(key);
    }

    /**
     * TODO...
     * @return
     */
    Set<String> getKeySet() {
        return names.keySet();
    }

    /**
     * TODO...
     * @return
     */
    SortedSet<String> getBoundedSortedKeySet() {
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
    boolean keyIsBounded(String key) {
        return !names.<String>get(key).equals(EdgeType.EMPTY.getName());
    }

    /**
     * TODO...
     * @param v
     * @return
     */
    boolean valueIsBounded(String v) {
        final Collection<String> verz = names.values();

        for (String s : verz) {
            if (s.equals(v)) {
                return true;
            }
        }

        return false;
    }
}
