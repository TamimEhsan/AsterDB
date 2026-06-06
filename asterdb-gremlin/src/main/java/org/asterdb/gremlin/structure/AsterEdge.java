package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        throw new UnsupportedOperationException("AsterEdge.outVertex() not yet implemented");
    }

    @Override
    public Vertex inVertex() {
        throw new UnsupportedOperationException("AsterEdge.inVertex() not yet implemented");
    }

    @Override
    public Iterator<Vertex> vertices(final Direction direction) {
        throw new UnsupportedOperationException("AsterEdge.vertices() not yet implemented");
    }

    @Override
    public <V> Property<V> property(final String key, final V value) {
        throw new UnsupportedOperationException("AsterEdge.property() not yet implemented");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Iterator<Property<V>> properties(final String... propertyKeys) {
        return Collections.emptyIterator();
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
        throw new UnsupportedOperationException("AsterEdge.remove() not yet implemented");
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
