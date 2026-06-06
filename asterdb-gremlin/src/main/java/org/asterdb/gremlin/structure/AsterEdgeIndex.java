package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;

import java.util.ArrayList;
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
        long[] edgeArray = graph.getStore().getEdgeWithProperty(key, String.valueOf(value));
        List<AsterEdge> result = new ArrayList<>();
        for (int i = 0; i < edgeArray.length; i += 2) {
            long source = edgeArray[i];
            long target = edgeArray[i + 1];
            Object edgeId = target + "-" + source;
            AsterEdge edge = new AsterEdge(edgeId, source, Edge.DEFAULT_LABEL, target, graph);
            Property<Object> property = new AsterProperty<>(edge, key, value);
            edge.properties.put(key, property);
            result.add(edge);
        }
        return result;
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
