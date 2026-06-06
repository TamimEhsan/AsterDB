package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AsterEdge extends AsterElement implements Edge {

    private final long outVertexId;
    private final long inVertexId;
    private final AsterGraph graph;
    private Map<String, Property> properties;

    public AsterEdge(final Object id, final long outVertexId, final String label, final long inVertexId, final AsterGraph graph) {
        super(id, label);
        this.outVertexId = outVertexId;
        this.inVertexId = inVertexId;
        this.graph = graph;
        this.properties = new HashMap<>();
    }

    @Override
    public Vertex outVertex() {
        return this.graph.vertex(this.outVertexId);
    }

    @Override
    public Vertex inVertex() {
        return this.graph.vertex(this.inVertexId);
    }

    @Override
    public Iterator<Vertex> vertices(final Direction direction) {
        switch (direction) {
            case OUT:
                return IteratorUtils.of(outVertex());
            case IN:
                return IteratorUtils.of(inVertex());
            default:
                return IteratorUtils.of(outVertex(), inVertex());
        }
    }

    @Override
    public <V> Property<V> property(final String key, final V value) {
        graph.addEdgeProperty(this.id, key, value);
        final Property<V> newProperty = new AsterProperty<>(this, key, value);
        this.properties.put(key, newProperty);
        return newProperty;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Iterator<Property<V>> properties(final String... propertyKeys) {
        if (this.properties == null) return Collections.emptyIterator();
        if (propertyKeys.length == 1) {
            if (propertyKeys[0] == null) return Collections.emptyIterator();
            final Property<V> property = this.properties.get(propertyKeys[0]);
            return property == null ? Collections.emptyIterator() : IteratorUtils.of(property);
        } else {
            return (Iterator) this.properties.entrySet().stream()
                    .filter(entry -> ElementHelper.keyExists(entry.getKey(), propertyKeys))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList()).iterator();
        }
    }

    @Override
    public Set<String> keys() {
        if (this.properties == null) return Collections.emptySet();
        return this.properties.keySet();
    }

    @Override
    public Graph graph() {
        return this.graph;
    }

    @Override
    public void remove() {
        this.graph.removeEdge(this.id);
    }

    @Override
    public String toString() {
        return StringFactory.edgeString(this);
    }

    public long outVertexId() {
        return this.outVertexId;
    }

    public long inVertexId() {
        return this.inVertexId;
    }
}
