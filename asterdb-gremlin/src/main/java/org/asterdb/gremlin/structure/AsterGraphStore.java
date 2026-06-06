package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Edge;

import java.util.Set;

public class AsterGraphStore {

    private final AsterGraph graph;

    AsterGraphStore(final AsterGraph graph) {
        this.graph = graph;
    }

    public AsterVertex addVertex(final long id) {
        throw new UnsupportedOperationException("AsterGraphStore.addVertex() not yet implemented");
    }

    public void removeVertex(final long vertexId) {
        throw new UnsupportedOperationException("AsterGraphStore.removeVertex() not yet implemented");
    }

    public AsterVertex getVertex(final long id) {
        throw new UnsupportedOperationException("AsterGraphStore.getVertex() not yet implemented");
    }

    public long vertexCount() {
        throw new UnsupportedOperationException("AsterGraphStore.vertexCount() not yet implemented");
    }

    public AsterEdge addEdge(final AsterVertex source, final AsterVertex target) {
        throw new UnsupportedOperationException("AsterGraphStore.addEdge() not yet implemented");
    }

    public void removeEdge(final long outVertexId, final long inVertexId) {
        throw new UnsupportedOperationException("AsterGraphStore.removeEdge() not yet implemented");
    }

    public long edgeCount() {
        throw new UnsupportedOperationException("AsterGraphStore.edgeCount() not yet implemented");
    }

    public Set<Edge> getOutNeighbours(final long vertexId) {
        throw new UnsupportedOperationException("AsterGraphStore.getOutNeighbours() not yet implemented");
    }

    public Set<Edge> getInNeighbours(final long vertexId) {
        throw new UnsupportedOperationException("AsterGraphStore.getInNeighbours() not yet implemented");
    }

    public void addVertexProperty(final long vertexId, final String key, final Object value) {
        throw new UnsupportedOperationException("AsterGraphStore.addVertexProperty() not yet implemented");
    }

    public void addEdgeProperty(final long outVertexId, final long inVertexId, final String key, final Object value) {
        throw new UnsupportedOperationException("AsterGraphStore.addEdgeProperty() not yet implemented");
    }

    public void close() {
        throw new UnsupportedOperationException("AsterGraphStore.close() not yet implemented");
    }
}
