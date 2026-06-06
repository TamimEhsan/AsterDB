package org.asterdb.gremlin.process.traversal.step.sideEffect;

import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.step.HasContainerHolder;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.HasContainer;
import org.apache.tinkerpop.gremlin.process.traversal.util.AndP;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.CloseableIterator;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.asterdb.gremlin.structure.AsterGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class AsterGraphStep<S, E extends Element> extends GraphStep<S, E> implements HasContainerHolder, AutoCloseable {

    private final List<HasContainer> hasContainers = new ArrayList<>();
    private final List<Iterator> iterators = new ArrayList<>();

    public AsterGraphStep(final GraphStep<S, E> originalGraphStep) {
        super(originalGraphStep.getTraversal(), originalGraphStep.getReturnClass(), originalGraphStep.isStartStep(), (Object[]) originalGraphStep.getIds());
        originalGraphStep.getLabels().forEach(this::addLabel);
        this.setIteratorSupplier(() -> (Iterator<E>) (Vertex.class.isAssignableFrom(this.returnClass) ? this.vertices() : this.edges()));
    }

    private Iterator<? extends Vertex> vertices() {
        final AsterGraph graph = (AsterGraph) this.getTraversal().getGraph().get();
        final HasContainer indexedContainer = getIndexKey(Vertex.class);
        Iterator<? extends Vertex> iterator;
        final Object[] resolvedIds = this.getIdsAsValues();

        if (null == resolvedIds)
            iterator = Collections.emptyIterator();
        else if (this.ids.length > 0)
            iterator = this.iteratorList(graph.vertices(this.ids));
        else {
            iterator = (null == indexedContainer ?
                    this.iteratorList(graph.vertices()) :
                    IteratorUtils.filter(
                            graph.queryVertexIndex(indexedContainer.getKey(), indexedContainer.getPredicate().getValue()).iterator(),
                            vertex -> HasContainer.testAll(vertex, this.hasContainers)));
        }

        iterators.add(iterator);
        return iterator;
    }

    private Iterator<? extends Edge> edges() {
        final AsterGraph graph = (AsterGraph) this.getTraversal().getGraph().get();
        final HasContainer indexedContainer = getIndexKey(Edge.class);
        Iterator<Edge> iterator;
        final Object[] resolvedIds = this.getIdsAsValues();

        if (null == resolvedIds)
            iterator = Collections.emptyIterator();
        else if (resolvedIds.length > 0)
            iterator = this.iteratorList(graph.edges(resolvedIds));
        else
            iterator = null == indexedContainer ?
                    this.iteratorList(graph.edges()) :
                    graph.queryEdgeIndex(indexedContainer.getKey(), indexedContainer.getPredicate().getValue()).stream()
                            .filter(edge -> HasContainer.testAll(edge, this.hasContainers))
                            .collect(java.util.stream.Collectors.<Edge>toList()).iterator();

        iterators.add(iterator);
        return iterator;
    }

    private HasContainer getIndexKey(final Class<? extends Element> indexedClass) {
        final AsterGraph graph = (AsterGraph) this.getTraversal().getGraph().get();
        final Set<String> indexedKeys = graph.getIndexedKeys(indexedClass);

        final Iterator<HasContainer> itty = IteratorUtils.filter(hasContainers.iterator(),
                c -> c.getPredicate().getBiPredicate() == Compare.eq && indexedKeys.contains(c.getKey()));
        return itty.hasNext() ? itty.next() : null;
    }

    private <E extends Element> Iterator<E> iteratorList(final Iterator<E> iterator) {
        final List<E> list = new ArrayList<>();
        try {
            while (iterator.hasNext()) {
                final E e = iterator.next();
                if (HasContainer.testAll(e, this.hasContainers))
                    list.add(e);
            }
        } finally {
            CloseableIterator.closeIterator(iterator);
        }
        return list.iterator();
    }

    @Override
    public String toString() {
        if (this.hasContainers.isEmpty())
            return super.toString();
        else
            return (null == this.ids || 0 == this.ids.length) ?
                    StringFactory.stepString(this, this.returnClass.getSimpleName().toLowerCase(), this.hasContainers) :
                    StringFactory.stepString(this, this.returnClass.getSimpleName().toLowerCase(), Arrays.toString(this.ids), this.hasContainers);
    }

    @Override
    public List<HasContainer> getHasContainers() {
        return Collections.unmodifiableList(this.hasContainers);
    }

    @Override
    public void addHasContainer(final HasContainer hasContainer) {
        if (hasContainer.getPredicate() instanceof AndP) {
            for (final P<?> predicate : ((AndP<?>) hasContainer.getPredicate()).getPredicates()) {
                this.addHasContainer(new HasContainer(hasContainer.getKey(), predicate));
            }
        } else
            this.hasContainers.add(hasContainer);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.hasContainers.hashCode();
    }

    @Override
    public void close() {
        iterators.forEach(CloseableIterator::closeIterator);
    }
}
