package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AsterGraphStore {

    private final RocksGraph db;
    private final AsterGraph graph;

    AsterGraphStore(final RocksGraph db, final AsterGraph graph) {
        this.db = db;
        this.graph = graph;
    }

    static RocksGraph openDatabase(final int updatePolicy) {
        try {
            RocksDB.loadLibrary();
            Options options = new Options();
            options.setCreateIfMissing(true);
            return RocksGraph.open(options, updatePolicy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open RocksGraph", e);
        }
    }

    public AsterVertex addVertex(final long id) {
        try {
            db.AddVertex(id);
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to add vertex " + id, e);
        }
        return new AsterVertex(id, graph);
    }

    public void removeVertex(final long vertexId) {
        // Current implementation is a no-op in RocksVertices
    }

    public AsterVertex getVertex(final long id) {
        return new AsterVertex(id, graph);
    }

    public long vertexCount() {
        try {
            return db.CountVertex();
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to count vertices", e);
        }
    }

    public AsterEdge addEdge(final AsterVertex source, final AsterVertex target) {
        long srcId = Long.parseLong(String.valueOf(source.id()));
        long tgtId = Long.parseLong(String.valueOf(target.id()));
        try {
            db.AddEdge(srcId, tgtId);
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to add edge", e);
        }
        Object edgeId = srcId + "-" + tgtId;
        return new AsterEdge(edgeId, srcId, Edge.DEFAULT_LABEL, tgtId, graph);
    }

    public void removeEdge(final Object edgeId) {
        String[] parts = edgeId.toString().split("-");
        long outVertexId = Long.parseLong(parts[0]);
        long inVertexId = Long.parseLong(parts[1]);
        try {
            db.DeleteEdge(outVertexId, inVertexId);
        } catch (Exception e) {
            // match original behavior: silently ignore
        }
    }

    public long edgeCount() {
        try {
            return db.CountEdge();
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to count edges", e);
        }
    }

    public Set<Edge> getOutNeighbours(final long vertexId) {
        Set<Edge> edgeList = new HashSet<>();
        try {
            long[] targets = db.GetOutNeighbours(vertexId);
            for (long target : targets) {
                Object edgeId = vertexId + "-" + target;
                edgeList.add(new AsterEdge(edgeId, vertexId, Edge.DEFAULT_LABEL, target, graph));
            }
            return edgeList;
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to get out neighbours", e);
        }
    }

    public Set<Edge> getInNeighbours(final long vertexId) {
        Set<Edge> edgeList = new HashSet<>();
        try {
            long[] sources = db.GetInNeighbours(vertexId);
            for (long source : sources) {
                Object edgeId = source + "-" + vertexId;
                edgeList.add(new AsterEdge(edgeId, source, Edge.DEFAULT_LABEL, vertexId, graph));
            }
            return edgeList;
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to get in neighbours", e);
        }
    }

    public void addVertexProperty(final long vertexId, final String key, final Object value) {
        try {
            db.AddVertexProperty(vertexId, key, String.valueOf(value));
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to add vertex property", e);
        }
    }

    public void addEdgeProperty(final Object edgeId, final String key, final Object value) {
        String[] parts = edgeId.toString().split("-");
        long outVertexId = Long.parseLong(parts[0]);
        long inVertexId = Long.parseLong(parts[1]);
        try {
            db.AddEdgeProperty(outVertexId, inVertexId, key, String.valueOf(value));
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to add edge property", e);
        }
    }

    public long[] getVertexWithProperty(final String key, final String value) {
        try {
            return db.GetVertexWithProperty(key, value);
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to query vertex index", e);
        }
    }

    public long[] getEdgeWithProperty(final String key, final String value) {
        try {
            return db.GetEdgeWithProperty(key, value);
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to query edge index", e);
        }
    }

    public void setWorkload(final float readRatio) {
        try {
            db.SetWorkload(readRatio);
        } catch (Exception e) {
            // match original: ignore
        }
    }

    public void setCacheMissRate(final double ratio) {
        try {
            db.SetCacheMissRate(ratio);
        } catch (Exception e) {
            // match original: ignore
        }
    }

    public void close() {
        try {
            db.terminate();
        } catch (Exception e) {
            // match original: print and continue
            System.out.println(e);
        }
    }
}
