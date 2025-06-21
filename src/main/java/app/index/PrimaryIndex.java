package app.index;

import java.util.HashMap;
import java.util.Map;

public class PrimaryIndex {
    private Map<String, Long> index = new HashMap<>(); // ISBN -> posição no arquivo

    public void add(String isbn, long pos) {
        index.put(isbn, pos);
    }

    public void remove(String isbn) {
        index.remove(isbn);
    }

    public Long get(String isbn) {
        return index.get(isbn);
    }

    public Map<String, Long> getIndex() {
        return index;
    }
} 