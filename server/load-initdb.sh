#!/bin/bash
#
# NOTE: THIS FILE IS GENERATED VIA "update.sh"
# DO NOT EDIT IT DIRECTLY; CHANGES WILL BE OVERWRITTEN.
#
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

# exit early if directory is empty
echo "Executing load-initdb.sh"
if ! [ "$(ls -A /docker-entrypoint-initdb.d)" ]; then
    exit 0
fi

# wait for JanusGraph Server
if ! [ -z "${JANUS_SERVER_TIMEOUT:-}" ]; then
    timeout "${JANUS_SERVER_TIMEOUT}s" bash -c \
    "until true &>/dev/null </dev/tcp/127.0.0.1/8182; do echo \"waiting for JanusGraph Server...\"; sleep 5; done"
fi

for f in /docker-entrypoint-initdb.d/*; do
  case "$f" in
    *.groovy) echo "$0: running $f"; ${JANUS_HOME}/bin/gremlin.sh -e "$f"; echo ;;
    *)        echo "$0: ignoring $f" ;;
  esac
  echo

echo "Finished load-initdb.sh"
done
