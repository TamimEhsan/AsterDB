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

# Ensure asterdb-gremlin jar is on the console's ext path.
ASTERDB_EXT="$GREMLIN_CONSOLE_HOME/ext/asterdb-gremlin"
ASTERDB_JAR="$REPO_ROOT/asterdb-gremlin/target/asterdb-gremlin-1.0.0-SNAPSHOT.jar"
if [ -f "$ASTERDB_JAR" ] && [ ! -L "$ASTERDB_EXT" ]; then
    mkdir -p "$ASTERDB_EXT/plugin" "$ASTERDB_EXT/lib"
    cp "$ASTERDB_JAR" "$ASTERDB_EXT/plugin/" 2>/dev/null || true
    cp "$ASTERDB_JAR" "$ASTERDB_EXT/lib/" 2>/dev/null || true
fi

exec "$GREMLIN_CONSOLE_HOME/bin/gremlin.sh" "$@"
