package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.ElementHelper;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AsterVertex extends AsterElement implements Vertex {

    private final AsterGraph graph;
    Map<String, List<VertexProperty>> properties;
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
        if (inVertex == null) throw Graph.Exceptions.argumentCanNotBeNull("inVertex");
        if (this.removed || ((AsterVertex) inVertex).removed)
            throw new IllegalStateException("Vertex has been removed");
        return graph.addEdge(this, (AsterVertex) inVertex, label, keyValues);
    }

    @Override
    public <V> VertexProperty<V> property(final VertexProperty.Cardinality cardinality, final String key, final V value, final Object... keyValues) {
        graph.addVertexProperty(this.id, key, value);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> VertexProperty<V> property(final String key) {
        if (this.properties == null) return VertexProperty.empty();
        final List<VertexProperty> props = this.properties.getOrDefault(key, Collections.emptyList());
        if (props.isEmpty()) return VertexProperty.empty();
        return props.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Iterator<VertexProperty<V>> properties(final String... propertyKeys) {
        if (this.removed) return Collections.emptyIterator();
        if (this.properties == null) return Collections.emptyIterator();
        if (propertyKeys.length == 1) {
            if (propertyKeys[0] == null) return Collections.emptyIterator();
            final List<VertexProperty> props = this.properties.getOrDefault(propertyKeys[0], Collections.emptyList());
            if (props.size() == 1) {
                return IteratorUtils.of(props.get(0));
            } else if (props.isEmpty()) {
                return Collections.emptyIterator();
            } else {
                return (Iterator) new ArrayList<>(props).iterator();
            }
        } else {
            return (Iterator) this.properties.entrySet().stream()
                    .filter(entry -> ElementHelper.keyExists(entry.getKey(), propertyKeys))
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toList()).iterator();
        }
    }

    @Override
    public Set<String> keys() {
        if (this.properties == null) return Collections.emptySet();
        return this.properties.keySet();
    }

    @Override
    public Iterator<Edge> edges(final Direction direction, final String... edgeLabels) {
        final List<Edge> edgeList = new ArrayList<>();
        long vertexId = Long.parseLong(String.valueOf(this.id));

        if (direction.equals(Direction.OUT) || direction.equals(Direction.BOTH)) {
            if (!outEdgesFetched) {
                outEdges = new HashMap<>();
                outEdges.put(Edge.DEFAULT_LABEL, graph.getStore().getOutNeighbours(vertexId));
                outEdgesFetched = true;
            }
            final Set<Edge> edgeSet = outEdges.get(Edge.DEFAULT_LABEL);
            if (edgeSet != null) edgeList.addAll(edgeSet);
        }

        if (direction.equals(Direction.IN) || direction.equals(Direction.BOTH)) {
            if (!inEdgesFetched) {
                inEdges = new HashMap<>();
                inEdges.put(Edge.DEFAULT_LABEL, graph.getStore().getInNeighbours(vertexId));
                inEdgesFetched = true;
            }
            final Set<Edge> edgeSet = inEdges.get(Edge.DEFAULT_LABEL);
            if (edgeSet != null) edgeList.addAll(edgeSet);
        }

        return edgeList.iterator();
    }

    @Override
    public Iterator<Vertex> vertices(final Direction direction, final String... edgeLabels) {
        if (direction.equals(Direction.BOTH)) {
            return IteratorUtils.concat(
                    IteratorUtils.map(this.edges(Direction.OUT, edgeLabels), Edge::inVertex),
                    IteratorUtils.map(this.edges(Direction.IN, edgeLabels), Edge::outVertex));
        }
        return IteratorUtils.map(this.edges(direction, edgeLabels),
                direction.equals(Direction.OUT) ? Edge::inVertex : Edge::outVertex);
    }

    @Override
    public Graph graph() {
        return this.graph;
    }

    @Override
    public void remove() {
        this.removed = true;
        graph.removeVertex(this.id);
    }

    @Override
    public String toString() {
        return StringFactory.vertexString(this);
    }
}
