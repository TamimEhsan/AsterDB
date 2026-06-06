package org.asterdb.gremlin;

import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.AbstractGraphProvider;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.asterdb.gremlin.structure.AsterEdge;
import org.asterdb.gremlin.structure.AsterElement;
import org.asterdb.gremlin.structure.AsterGraph;
import org.asterdb.gremlin.structure.AsterProperty;
import org.asterdb.gremlin.structure.AsterVertex;
import org.asterdb.gremlin.structure.AsterVertexProperty;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AsterGraphProvider extends AbstractGraphProvider {

    private static final Set<Class> IMPLEMENTATIONS = new HashSet<>() {{
        add(AsterGraph.class);
        add(AsterVertex.class);
        add(AsterEdge.class);
        add(AsterElement.class);
        add(AsterProperty.class);
        add(AsterVertexProperty.class);
    }};

    @Override
    public Map<String, Object> getBaseConfiguration(final String graphName, final Class<?> test,
                                                     final String testMethodName, final Graph.Variables.Exceptions exceptionType) {
        return Map.of(
                Graph.GRAPH, AsterGraph.class.getName(),
                "updatePolicy", 0
        );
    }

    @Override
    public void clear(final Graph graph, final Configuration configuration) throws Exception {
        if (graph != null) {
            graph.close();
        }
    }

    @Override
    public Set<Class> getImplementations() {
        return IMPLEMENTATIONS;
    }
}
