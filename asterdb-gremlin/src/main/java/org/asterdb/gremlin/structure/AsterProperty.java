package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;

import java.util.NoSuchElementException;

public class AsterProperty<V> implements Property<V> {

    private final Element element;
    private final String key;
    private final V value;

    public AsterProperty(final Element element, final String key, final V value) {
        this.element = element;
        this.key = key;
        this.value = value;
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
    public Element element() {
        return this.element;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("AsterProperty.remove() not yet implemented");
    }

    @Override
    public String toString() {
        return StringFactory.propertyString(this);
    }
}
