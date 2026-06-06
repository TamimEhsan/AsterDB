package org.asterdb.gremlin.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsterEdgeIndex {

    private final AsterGraph graph;
    private final Set<String> indexedKeys = new HashSet<>();

    AsterEdgeIndex(final AsterGraph graph) {
        this.graph = graph;
    }

    public List<AsterEdge> get(final String key, final Object value) {
        throw new UnsupportedOperationException("AsterEdgeIndex.get() not yet implemented");
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
