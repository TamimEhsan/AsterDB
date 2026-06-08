#!/bin/bash
set -e

# Honor an existing JAVA_HOME; otherwise derive it from javac/java on PATH so
# this works across architectures (amd64/arm64) and distros, not just the
# Debian/Ubuntu amd64 default path.
if [ -z "$JAVA_HOME" ]; then
    JAVA_BIN=$(command -v javac || command -v java)
    if [ -z "$JAVA_BIN" ]; then
        echo "JAVA_HOME is not set and no javac/java found on PATH." >&2
        exit 1
    fi
    JAVA_HOME=$(dirname "$(dirname "$(readlink -f "$JAVA_BIN")")")
fi
export JAVA_HOME
echo "Using JAVA_HOME=$JAVA_HOME"

cd GraphKV
sudo make clean
sudo DEBUG_LEVEL=0 DISABLE_WARNING_AS_ERROR=1 JAVA_HOME="$JAVA_HOME" EXTRA_CXXFLAGS=-fPIC make -j10 install-static
sudo DEBUG_LEVEL=0 DISABLE_WARNING_AS_ERROR=1 JAVA_HOME="$JAVA_HOME" EXTRA_CXXFLAGS=-fPIC make -j10 install-shared
sudo DEBUG_LEVEL=0 DISABLE_WARNING_AS_ERROR=1 JAVA_HOME="$JAVA_HOME" EXTRA_CXXFLAGS=-fPIC make -j10 rocksdbjava

cd tools
g++ bulkload.cc -lrocksdb -lgflags -o ../../bulkload -std=c++17 -g

cd ../..
mvn install:install-file \
    -Dfile=GraphKV/java/target/rocksdbjni-8.9.0-linux64.jar -DgroupId=org.rocksdb -DartifactId=rocksdbjni -Dversion=8.9.0 \
    -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository/
# Build the provider and stage its runtime dependencies (rocksdbjni, commons-io)
# into target/dependency so bin/gremlin.sh can install them into the Gremlin
# Console's ext/asterdb-gremlin/plugin/ (the console only puts plugin/* on its
# classpath, not lib/).
mvn clean install dependency:copy-dependencies -pl asterdb-gremlin \
    -DincludeScope=runtime -Dmaven.test.skip=true