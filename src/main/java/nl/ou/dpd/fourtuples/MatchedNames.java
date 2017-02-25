package nl.ou.dpd.fourtuples;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author E.M. van Doorn
 */

public class MatchedNames {
    // Contains class/interface names of se mapped on class/interface names of
    // the design pattern.
    // se-names --> dp-names.

    private HashMap<String, String> names;

    MatchedNames() {
        names = new HashMap();
    }

    MatchedNames(MatchedNames nm) {
        this();

        Set<String> s = nm.getKeySet();
        for (String name : s) {
            add(name, nm.get(name));
        }
    }

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


    boolean isEmpty(String key) {
        return names.get(key).equals(EdgeType.EMPTY.getName());
    }


    boolean equals(String key, String value) {
        return get(key).equals(value);
    }


    void add(String key, String value) {
        names.<String, String>put(key, value);
    }

    void add(String key) {
        add(key, EdgeType.EMPTY.getName());
    }


    String get(String key) {
        return names.<String>get(key);
    }


    Set<String> getKeySet() {
        return names.keySet();
    }

    /**
     *
     * @return
     */
    SortedSet<String> getBoundedSortedKeySet() {
        return new TreeSet(getKeySet()
                .stream()
                .filter(key -> keyIsBounded(key))
                .collect(Collectors.toSet()));
    }


    boolean keyIsBounded(String key) {
        return !names.<String>get(key).equals(EdgeType.EMPTY.getName());
    }


    boolean valueIsBounded(String v) {
        Collection<String> verz;

        verz = names.values();

        for (String s : verz) {
            if (s.equals(v)) {
                return true;
            }
        }

        return false;
    }
}
