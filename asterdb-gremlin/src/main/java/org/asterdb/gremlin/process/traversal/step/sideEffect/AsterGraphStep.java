package org.asterdb.gremlin.process.traversal.step.sideEffect;

import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class AsterGraphStep<S, E extends Element> extends GraphStep<S, E> {

    public AsterGraphStep(final GraphStep<S, E> originalGraphStep) {
        super(originalGraphStep.getTraversal(), originalGraphStep.getReturnClass(), originalGraphStep.isStartStep(), (Object[]) originalGraphStep.getIds());
        originalGraphStep.getLabels().forEach(this::addLabel);
    }
}
