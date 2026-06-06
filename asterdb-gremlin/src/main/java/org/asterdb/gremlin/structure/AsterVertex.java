package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AsterVertex extends AsterElement implements Vertex {

    private final AsterGraph graph;
    private Map<String, List<VertexProperty>> properties;
    private Map<String, Set<Edge>> outEdges;
    private Map<String, Set<Edge>> inEdges;
    private boolean outEdgesFetched = false;
    private boolean inEdgesFetched = false;

    public AsterVertex(final Object id, final AsterGraph graph) {
        super(id, Vertex.DEFAULT_LABEL);
        this.graph = graph;
        this.properties = new HashMap<>();
    }

    public AsterVertex(final Object id, final String label, final AsterGraph graph) {
        super(id, label);
        this.graph = graph;
        this.properties = new HashMap<>();
    }

    @Override
    public Edge addEdge(final String label, final Vertex inVertex, final Object... keyValues) {
        throw new UnsupportedOperationException("AsterVertex.addEdge() not yet implemented");
    }

    @Override
    public <V> VertexProperty<V> property(final VertexProperty.Cardinality cardinality, final String key, final V value, final Object... keyValues) {
        throw new UnsupportedOperationException("AsterVertex.property() not yet implemented");
    }

    @Override
    public <V> VertexProperty<V> property(final String key) {
        throw new UnsupportedOperationException("AsterVertex.property(key) not yet implemented");
    }

    @Override
    public <V> Iterator<VertexProperty<V>> properties(final String... propertyKeys) {
        return Collections.emptyIterator();
    }

    @Override
    public Set<String> keys() {
        if (this.properties == null) return Collections.emptySet();
        return this.properties.keySet();
    }

    @Override
    public Iterator<Edge> edges(final Direction direction, final String... edgeLabels) {
        throw new UnsupportedOperationException("AsterVertex.edges() not yet implemented");
    }

    @Override
    public Iterator<Vertex> vertices(final Direction direction, final String... edgeLabels) {
        throw new UnsupportedOperationException("AsterVertex.vertices() not yet implemented");
    }

    @Override
    public Graph graph() {
        return this.graph;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("AsterVertex.remove() not yet implemented");
    }

    @Override
    public String toString() {
        return StringFactory.vertexString(this);
    }
}
