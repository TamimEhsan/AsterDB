package org.asterdb.gremlin.jsr223;

import org.apache.tinkerpop.gremlin.jsr223.AbstractGremlinPlugin;
import org.apache.tinkerpop.gremlin.jsr223.DefaultImportCustomizer;
import org.apache.tinkerpop.gremlin.jsr223.ImportCustomizer;
import org.asterdb.gremlin.structure.AsterEdge;
import org.asterdb.gremlin.structure.AsterGraph;
import org.asterdb.gremlin.structure.AsterProperty;
import org.asterdb.gremlin.structure.AsterVertex;
import org.asterdb.gremlin.structure.AsterVertexProperty;

public final class AsterGremlinPlugin extends AbstractGremlinPlugin {
    private static final String NAME = "asterdb.gremlin";

    private static final ImportCustomizer imports = DefaultImportCustomizer.build()
            .addClassImports(
                    AsterGraph.class,
                    AsterVertex.class,
                    AsterEdge.class,
                    AsterProperty.class,
                    AsterVertexProperty.class).create();

    private static final AsterGremlinPlugin instance = new AsterGremlinPlugin();

    public AsterGremlinPlugin() {
        super(NAME, imports);
    }

    public static AsterGremlinPlugin instance() {
        return instance;
    }
}
