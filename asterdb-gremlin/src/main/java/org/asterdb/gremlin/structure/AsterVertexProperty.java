package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AsterVertexProperty<V> implements VertexProperty<V> {

    private final Object id;
    private final String key;
    private final V value;
    private final Vertex vertex;

    public AsterVertexProperty(final Object id, final String key, final V value, final Vertex vertex) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.vertex = vertex;
    }

    public AsterVertexProperty(final String key, final V value) {
        this(null, key, value, null);
    }

    @Override
    public Object id() {
        return this.id;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public V value() throws NoSuchElementException {
        return this.value;
    }

    @Override
    public boolean isPresent() {
        return this.value != null;
    }

    @Override
    public Vertex element() {
        return this.vertex;
    }

    @Override
    public <U> Iterator<Property<U>> properties(final String... propertyKeys) {
        return Collections.emptyIterator();
    }

    @Override
    public <U> Property<U> property(final String key, final U value) {
        throw new UnsupportedOperationException("Meta-properties not supported");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("AsterVertexProperty.remove() not yet implemented");
    }

    @Override
    public String label() {
        return this.key;
    }

    @Override
    public Graph graph() {
        return this.vertex != null ? this.vertex.graph() : null;
    }

    @Override
    public String toString() {
        return StringFactory.propertyString(this);
    }
}
