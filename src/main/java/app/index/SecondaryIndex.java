package app.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecondaryIndex {
    private Map<String, Set<String>> index = new HashMap<>(); // título -> conjunto de ISBNs

    public void add(String titulo, String isbn) {
        index.computeIfAbsent(titulo, k -> new HashSet<>()).add(isbn);
    }

    public void remove(String titulo, String isbn) {
        Set<String> isbns = index.get(titulo);
        if (isbns != null) {
            isbns.remove(isbn);
            if (isbns.isEmpty()) {
                index.remove(titulo);
            }
        }
    }

    public Set<String> get(String titulo) {
        return index.getOrDefault(titulo, new HashSet<>());
    }

    public Map<String, Set<String>> getIndex() {
        return index;
    }
} 