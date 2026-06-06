package org.asterdb.gremlin.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsterVertexIndex {

    private final AsterGraph graph;
    private final Set<String> indexedKeys = new HashSet<>();

    AsterVertexIndex(final AsterGraph graph) {
        this.graph = graph;
    }

    public List<AsterVertex> get(final String key, final Object value) {
        throw new UnsupportedOperationException("AsterVertexIndex.get() not yet implemented");
    }

    public void createKeyIndex(final String key) {
        this.indexedKeys.add(key);
    }

    public void dropKeyIndex(final String key) {
        this.indexedKeys.remove(key);
    }

    public Set<String> getIndexedKeys() {
        return this.indexedKeys;
    }
}
