#!/usr/bin/env bash
#
# AsterDB Gremlin Console launcher.
#
# Delegates to an Apache Gremlin Console binary distribution that must be
# available locally. The Dockerfile downloads it to /root/gremlin-console;
# for local development, set GREMLIN_CONSOLE_HOME to point at your unpacked
# apache-tinkerpop-gremlin-console-<version>-bin/ directory.
#
# The asterdb-gremlin jar (and its dependencies) is added to the console's
# /ext directory so AsterGraph is importable from the console.

set -e

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

if [ -z "${GREMLIN_CONSOLE_HOME:-}" ]; then
    if [ -d "/root/gremlin-console" ]; then
        GREMLIN_CONSOLE_HOME="/root/gremlin-console"
    else
        echo "GREMLIN_CONSOLE_HOME is not set and /root/gremlin-console does not exist." >&2
        echo "Download Apache Gremlin Console from https://tinkerpop.apache.org/downloads.html" >&2
        echo "and set GREMLIN_CONSOLE_HOME to its unpacked bin directory." >&2
        exit 1
    fi
fi

# Install AsterDB as a console plugin. The console only adds ext/<name>/plugin/*
# to its classpath, so the asterdb-gremlin jar AND its runtime dependencies
# (rocksdbjni, commons-io) must all live in plugin/.
#
# The Dockerfile pre-populates this at image-build time. For local development,
# this block syncs from the build output — run the following once first:
#   mvn -pl asterdb-gremlin package dependency:copy-dependencies -DincludeScope=runtime
ASTERDB_PLUGIN="$GREMLIN_CONSOLE_HOME/ext/asterdb-gremlin/plugin"
ASTERDB_TARGET="$REPO_ROOT/asterdb-gremlin/target"
ASTERDB_JAR=$(ls "$ASTERDB_TARGET"/asterdb-gremlin-*.jar 2>/dev/null | head -1 || true)
if [ -n "$ASTERDB_JAR" ]; then
    mkdir -p "$ASTERDB_PLUGIN"
    cp "$ASTERDB_JAR" "$ASTERDB_PLUGIN/" 2>/dev/null || true
    if [ -d "$ASTERDB_TARGET/dependency" ]; then
        cp "$ASTERDB_TARGET"/dependency/*.jar "$ASTERDB_PLUGIN/" 2>/dev/null || true
    fi
fi

exec "$GREMLIN_CONSOLE_HOME/bin/gremlin.sh" "$@"
