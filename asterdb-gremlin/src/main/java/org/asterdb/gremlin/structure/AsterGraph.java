package org.asterdb.gremlin.structure;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Iterator;

@Graph.OptIn(Graph.OptIn.SUITE_STRUCTURE_STANDARD)
public class AsterGraph implements Graph, AutoCloseable {

    private static final String GREMLIN_GRAPH = "gremlin.graph";
    private static final String ASTER_UPDATE_POLICY = "updatePolicy";

    private final Configuration configuration;
    private final AsterGraphFeatures features = new AsterGraphFeatures();
    private AsterGraphStore store;
    private long currentVertexId = 0L;

    private AsterGraph(final Configuration configuration) {
        this.configuration = configuration;
    }

    public static AsterGraph open() {
        final Configuration config = new BaseConfiguration();
        config.setProperty(GREMLIN_GRAPH, AsterGraph.class.getName());
        config.setProperty(ASTER_UPDATE_POLICY, 0);
        return open(config);
    }

    public static AsterGraph open(final int updatePolicy) {
        final Configuration config = new BaseConfiguration();
        config.setProperty(GREMLIN_GRAPH, AsterGraph.class.getName());
        config.setProperty(ASTER_UPDATE_POLICY, updatePolicy);
        return open(config);
    }

    public static AsterGraph open(final Configuration configuration) {
        return new AsterGraph(configuration);
    }

    // --- Graph interface ---

    @Override
    public Vertex addVertex(final Object... keyValues) {
        throw new UnsupportedOperationException("AsterGraph.addVertex() not yet implemented");
    }

    @Override
    public <C extends GraphComputer> C compute(final Class<C> graphComputerClass) throws IllegalArgumentException {
        throw Graph.Exceptions.graphComputerNotSupported();
    }

    @Override
    public GraphComputer compute() throws IllegalArgumentException {
        throw Graph.Exceptions.graphComputerNotSupported();
    }

    @Override
    public Iterator<Vertex> vertices(final Object... vertexIds) {
        throw new UnsupportedOperationException("AsterGraph.vertices() not yet implemented");
    }

    @Override
    public Iterator<Edge> edges(final Object... edgeIds) {
        throw new UnsupportedOperationException("AsterGraph.edges() not yet implemented");
    }

    @Override
    public Transaction tx() {
        throw Graph.Exceptions.transactionsNotSupported();
    }

    @Override
    public Variables variables() {
        throw Graph.Exceptions.variablesNotSupported();
    }

    @Override
    public Configuration configuration() {
        return this.configuration;
    }

    @Override
    public Features features() {
        return this.features;
    }

    @Override
    public void close() throws Exception {
        if (this.store != null) {
            this.store.close();
        }
    }

    // --- AsterDB-specific methods ---

    public Edge addEdge(final AsterVertex outVertex, final AsterVertex inVertex, final String label, final Object... keyValues) {
        throw new UnsupportedOperationException("AsterGraph.addEdge() not yet implemented");
    }

    public void removeVertex(final Object vertexId) {
        throw new UnsupportedOperationException("AsterGraph.removeVertex() not yet implemented");
    }

    public void removeEdge(final Object edgeId) {
        throw new UnsupportedOperationException("AsterGraph.removeEdge() not yet implemented");
    }

    public Vertex vertex(final Object vertexId) {
        throw new UnsupportedOperationException("AsterGraph.vertex() not yet implemented");
    }

    public void setWorkload(final float readRatio) {
        throw new UnsupportedOperationException("AsterGraph.setWorkload() not yet implemented");
    }

    public void setCacheMissRate(final double ratio) {
        throw new UnsupportedOperationException("AsterGraph.setCacheMissRate() not yet implemented");
    }

    public void addVertexProperty(final Object vertexId, final String key, final Object value) {
        throw new UnsupportedOperationException("AsterGraph.addVertexProperty() not yet implemented");
    }

    public void addEdgeProperty(final Object edgeId, final String key, final Object value) {
        throw new UnsupportedOperationException("AsterGraph.addEdgeProperty() not yet implemented");
    }

    public long getCurrentVertexId() {
        return this.currentVertexId;
    }
}
