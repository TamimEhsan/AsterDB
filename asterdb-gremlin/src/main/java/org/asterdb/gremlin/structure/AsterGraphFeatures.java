package org.asterdb.gremlin.structure;

import org.apache.tinkerpop.gremlin.structure.Graph;

public class AsterGraphFeatures implements Graph.Features {

    private final AsterVertexFeatures vertexFeatures = new AsterVertexFeatures();
    private final AsterEdgeFeatures edgeFeatures = new AsterEdgeFeatures();
    private final AsterGraphGraphFeatures graphFeatures = new AsterGraphGraphFeatures();

    @Override
    public VertexFeatures vertex() {
        return this.vertexFeatures;
    }

    @Override
    public EdgeFeatures edge() {
        return this.edgeFeatures;
    }

    @Override
    public GraphFeatures graph() {
        return this.graphFeatures;
    }

    @Override
    public String toString() {
        return String.format("AsterGraphFeatures[vertex=%s, edge=%s, graph=%s]",
                this.vertexFeatures, this.edgeFeatures, this.graphFeatures);
    }

    public static class AsterVertexFeatures implements VertexFeatures {

        private final AsterVertexPropertyFeatures vertexPropertyFeatures = new AsterVertexPropertyFeatures();

        @Override
        public VertexPropertyFeatures properties() {
            return this.vertexPropertyFeatures;
        }

        @Override
        public boolean supportsCustomIds() {
            return false;
        }

        @Override
        public boolean supportsUserSuppliedIds() {
            return false;
        }

        @Override
        public boolean supportsNumericIds() {
            return true;
        }
    }

    public static class AsterEdgeFeatures implements EdgeFeatures {

        private final AsterEdgePropertyFeatures edgePropertyFeatures = new AsterEdgePropertyFeatures();

        @Override
        public EdgePropertyFeatures properties() {
            return this.edgePropertyFeatures;
        }

        @Override
        public boolean supportsCustomIds() {
            return false;
        }

        @Override
        public boolean supportsUserSuppliedIds() {
            return false;
        }
    }

    public static class AsterGraphGraphFeatures implements GraphFeatures {

        @Override
        public boolean supportsComputer() {
            return false;
        }

        @Override
        public boolean supportsTransactions() {
            return false;
        }

        @Override
        public boolean supportsThreadedTransactions() {
            return false;
        }

        @Override
        public VariableFeatures variables() {
            return new AsterVariableFeatures();
        }
    }

    public static class AsterVertexPropertyFeatures implements VertexPropertyFeatures {
    }

    public static class AsterEdgePropertyFeatures implements EdgePropertyFeatures {
    }

    public static class AsterVariableFeatures implements VariableFeatures {

        @Override
        public boolean supportsVariables() {
            return false;
        }
    }
}
