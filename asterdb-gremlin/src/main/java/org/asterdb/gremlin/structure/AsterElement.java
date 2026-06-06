package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.Objects;

public abstract class AsterElement implements Element {

    protected final Object id;
    protected final String label;
    protected boolean removed = false;

    protected AsterElement(final Object id, final String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public Object id() {
        return this.id;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public abstract Graph graph();

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final AsterElement other = (AsterElement) obj;
        return Objects.equals(this.id, other.id);
    }
}
