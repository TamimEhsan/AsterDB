package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.ArrayList;
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
        long[] vertexArray = graph.getStore().getVertexWithProperty(key, String.valueOf(value));
        List<AsterVertex> result = new ArrayList<>();
        for (long vertexId : vertexArray) {
            AsterVertex vertex = new AsterVertex(vertexId, graph);
            AsterVertexProperty<Object> property = new AsterVertexProperty<>(key, value);
            List<VertexProperty> propertyList = new ArrayList<>();
            propertyList.add(property);
            vertex.properties.put(key, propertyList);
            result.add(vertex);
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
