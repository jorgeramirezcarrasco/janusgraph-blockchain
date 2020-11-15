#!/bin/bash

JANUS_PROPS="${JANUS_CONFIG_DIR}/janusgraph.properties"
GREMLIN_YAML="${JANUS_CONFIG_DIR}/gremlin-server.yaml"

# running as root; step down to run as janusgraph user
if [ "$1" == 'janusgraph' ] && [ "$(id -u)" == "0" ]; then
  mkdir -p ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}
  chown -R janusgraph:janusgraph ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}
  chmod 700 ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}

  exec chroot --skip-chdir --userspec janusgraph:janusgraph / "${BASH_SOURCE}" "$@"
fi

# running as non root user
if [ "$1" == 'janusgraph' ]; then
  # setup config directory
  mkdir -p ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}
  cp conf/gremlin-server/janusgraph-${JANUS_PROPS_TEMPLATE}-server.properties ${JANUS_CONFIG_DIR}/janusgraph.properties
  cp conf/gremlin-server/gremlin-server.yaml ${JANUS_CONFIG_DIR}
  chown -R "$(id -u):$(id -g)" ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}
  chmod 700 ${JANUS_DATA_DIR} ${JANUS_CONFIG_DIR}
  chmod -R 600 ${JANUS_CONFIG_DIR}/*

  if [ "$2" == 'show-config' ]; then
    echo "# contents of ${JANUS_PROPS}"
    cat "$JANUS_PROPS"
    echo "---------------------------------------"
    echo "# contents of ${GREMLIN_YAML}"
    cat "$GREMLIN_YAML"
    exit 0
  else
    # wait for storage
    if ! [ -z "${JANUS_STORAGE_TIMEOUT:-}" ]; then
      F="$(mktemp --suffix .groovy)"
      echo "graph = JanusGraphFactory.open('${JANUS_CONFIG_DIR}/janusgraph.properties')" > $F
      timeout "${JANUS_STORAGE_TIMEOUT}s" bash -c \
        "until bin/gremlin.sh -e $F > /dev/null 2>&1; do echo \"waiting for storage...\"; sleep 5; done"
      rm -f "$F"
    fi

    /usr/local/bin/load-initdb.sh &

    exec ${JANUS_HOME}/bin/gremlin-server.sh ${JANUS_CONFIG_DIR}/gremlin-server.yaml
    
  fi
fi

# override hosts for remote connections with Gremlin Console
if ! [ -z "${GREMLIN_REMOTE_HOSTS:-}" ]; then
  sed -i "s/hosts\s*:.*/hosts: [$GREMLIN_REMOTE_HOSTS]/" ${JANUS_HOME}/conf/remote.yaml
fi

exec "$@"