#
# NOTE: THIS FILE IS GENERATED VIA "update.sh"
# DO NOT EDIT IT DIRECTLY; CHANGES WILL BE OVERWRITTEN.
#
# Copyright 2019 JanusGraph Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM openjdk:8-jdk as builder

ARG JANUS_VERSION=0.5.2

ENV JANUS_VERSION=${JANUS_VERSION} \
    JANUS_HOME=/opt/janusgraph

RUN curl -fSL https://github.com/JanusGraph/janusgraph/releases/download/v${JANUS_VERSION}/janusgraph-${JANUS_VERSION}.zip -o janusgraph.zip && \
    curl -fSL https://github.com/JanusGraph/janusgraph/releases/download/v${JANUS_VERSION}/janusgraph-${JANUS_VERSION}.zip.asc -o janusgraph.zip.asc && \
    curl -fSL https://github.com/JanusGraph/janusgraph/releases/download/v${JANUS_VERSION}/KEYS -o KEYS && gpg --import KEYS && \
    gpg --batch --verify janusgraph.zip.asc janusgraph.zip && \
    unzip janusgraph.zip && \
    mv janusgraph-${JANUS_VERSION} /opt/janusgraph && \
    rm -rf ${JANUS_HOME}/elasticsearch && \
    rm -rf ${JANUS_HOME}/javadocs && \
    rm -rf ${JANUS_HOME}/log && \
    rm -rf ${JANUS_HOME}/examples

COPY conf/janusgraph-cql-es-server.properties \ 
conf/gremlin-server.yaml \
conf/log4j-server.properties \
${JANUS_HOME}/conf/gremlin-server/
COPY scripts/remote-connect.groovy scripts/empty-sample.groovy ${JANUS_HOME}/scripts/

FROM openjdk:8-jdk

ARG CREATED=test
ARG REVISION=test
ARG JANUS_VERSION=0.5.2

ENV JANUS_VERSION=${JANUS_VERSION} \
    JANUS_HOME=/opt/janusgraph \
    JANUS_CONFIG_DIR=/etc/opt/janusgraph \
    JANUS_DATA_DIR=/var/lib/janusgraph \
    JANUS_SERVER_TIMEOUT=30 \
    JANUS_STORAGE_TIMEOUT=60 \
    JANUS_PROPS_TEMPLATE=cql-es

RUN groupadd -r janusgraph --gid=999 && \
    useradd -r -g janusgraph --uid=999 janusgraph && \
    apt-get update -y && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y krb5-user && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir /docker-entrypoint-initdb.d

COPY --from=builder /opt/janusgraph/ /opt/janusgraph/

COPY docker-entrypoint.sh /usr/local/bin/
COPY load-initdb.sh /usr/local/bin/

RUN chmod 755 /usr/local/bin/docker-entrypoint.sh && \
    chmod 755 /usr/local/bin/load-initdb.sh && \
    chown -R janusgraph:janusgraph ${JANUS_HOME}

EXPOSE 8182

WORKDIR ${JANUS_HOME}

ENTRYPOINT [ "docker-entrypoint.sh" ]
CMD [ "janusgraph" ]