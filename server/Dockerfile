FROM janusgraph/janusgraph:latest

COPY conf/janusgraph-cql-configurationgraph.properties conf/janusgraph-cql-configurationgraph.properties 
COPY conf/gremlin-server/gremlin-server-configuration.yaml conf/gremlin-server/gremlin-server-configuration.yaml

CMD [ "bin/gremlin-server.sh", "conf/gremlin-server/gremlin-server-configuration.yaml"]