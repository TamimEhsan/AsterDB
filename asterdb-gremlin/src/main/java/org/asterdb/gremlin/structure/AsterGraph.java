package org.asterdb.gremlin.structure;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.rocksdb.RocksGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Graph.OptIn(Graph.OptIn.SUITE_STRUCTURE_STANDARD)
public class AsterGraph implements Graph, AutoCloseable {

    private static final String ASTER_UPDATE_POLICY = "updatePolicy";

    private final Configuration configuration;
    private final AsterGraphFeatures features = new AsterGraphFeatures();
    private final AsterGraphStore store;
    private final AsterVertexIndex vertexIndex;
    private final AsterEdgeIndex edgeIndex;
    private long currentVertexId = 0L;

    private AsterGraph(final Configuration configuration) {
        this.configuration = configuration;
        int updatePolicy = configuration.getInt(ASTER_UPDATE_POLICY, 0);
        RocksGraph db = AsterGraphStore.openDatabase(updatePolicy);
        this.store = new AsterGraphStore(db, this);
        this.vertexIndex = new AsterVertexIndex(this);
        this.edgeIndex = new AsterEdgeIndex(this);
        this.currentVertexId = store.vertexCount();
    }

    public static AsterGraph open() {
        final Configuration config = new BaseConfiguration();
        config.setProperty(Graph.GRAPH, AsterGraph.class.getName());
        config.setProperty(ASTER_UPDATE_POLICY, 0);
        return open(config);
    }

    public static AsterGraph open(final int updatePolicy) {
        final Configuration config = new BaseConfiguration();
        config.setProperty(Graph.GRAPH, AsterGraph.class.getName());
        config.setProperty(ASTER_UPDATE_POLICY, updatePolicy);
        return open(config);
    }

    public static AsterGraph open(final Configuration configuration) {
        return new AsterGraph(configuration);
    }

    // --- Graph interface ---

    @Override
    public Vertex addVertex(final Object... keyValues) {
        long id = currentVertexId;
        currentVertexId++;
        return store.addVertex(id);
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
        List<Object> idList = new ArrayList<>();
        if (vertexIds == null || vertexIds.length == 0) {
            for (long lid = 0L; lid < currentVertexId; lid++) {
                idList.add(lid);
            }
        } else {
            for (Object vid : vertexIds) {
                long lid = Long.parseLong(String.valueOf(vid));
                if (lid < currentVertexId) {
                    idList.add(lid);
                }
            }
        }
        return IteratorUtils.map(idList, this::vertex).iterator();
    }

    @Override
    public Iterator<Edge> edges(final Object... edgeIds) {
        // Edge iteration by ID is not fully supported in the current storage model
        return Collections.emptyIterator();
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
        this.store.close();
    }

    // --- AsterDB-specific methods ---

    public Edge addEdge(final AsterVertex outVertex, final AsterVertex inVertex, final String label, final Object... keyValues) {
        return store.addEdge(outVertex, inVertex);
    }

    public void removeVertex(final Object vertexId) {
        store.removeVertex(Long.parseLong(String.valueOf(vertexId)));
    }

    public void removeEdge(final Object edgeId) {
        store.removeEdge(edgeId);
    }

    public Vertex vertex(final Object vertexId) {
        return store.getVertex(Long.parseLong(String.valueOf(vertexId)));
    }

    public void setWorkload(final float readRatio) {
        store.setWorkload(readRatio);
    }

    public void setCacheMissRate(final double ratio) {
        store.setCacheMissRate(ratio);
    }

    public void addVertexProperty(final Object vertexId, final String key, final Object value) {
        store.addVertexProperty(Long.parseLong(String.valueOf(vertexId)), key, value);
    }

    public void addEdgeProperty(final Object edgeId, final String key, final Object value) {
        store.addEdgeProperty(edgeId, key, value);
    }

    public long getCurrentVertexId() {
        return this.currentVertexId;
    }

    AsterGraphStore getStore() {
        return this.store;
    }

    public Set<String> getIndexedKeys(final Class<? extends org.apache.tinkerpop.gremlin.structure.Element> elementClass) {
        if (Vertex.class.isAssignableFrom(elementClass)) {
            return vertexIndex.getIndexedKeys();
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            return edgeIndex.getIndexedKeys();
        } else {
            return Collections.emptySet();
        }
    }

    public List<? extends Vertex> queryVertexIndex(final String key, final Object value) {
        return vertexIndex.get(key, value);
    }

    public List<? extends Edge> queryEdgeIndex(final String key, final Object value) {
        return edgeIndex.get(key, value);
    }

    public void createIndex(final String key, final Class<? extends org.apache.tinkerpop.gremlin.structure.Element> elementClass) {
        if (Vertex.class.isAssignableFrom(elementClass)) {
            vertexIndex.createKeyIndex(key);
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            edgeIndex.createKeyIndex(key);
        }
    }

    public void dropIndex(final String key, final Class<? extends org.apache.tinkerpop.gremlin.structure.Element> elementClass) {
        if (Vertex.class.isAssignableFrom(elementClass)) {
            vertexIndex.dropKeyIndex(key);
        } else if (Edge.class.isAssignableFrom(elementClass)) {
            edgeIndex.dropKeyIndex(key);
        }
    }
}
