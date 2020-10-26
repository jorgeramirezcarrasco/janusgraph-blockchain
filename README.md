# JanusGraph for BlockChain Use Case

Server

```
docker build -t janusblockchain -f server/Dockerfile .

docker run -it -p 8182:8182 --name janusblockchain-default janusblockchain
```

Client

```
docker build -t janusblockchainclient -f client/Dockerfile .

docker run --rm --link janusblockchain-default:janusgraph -e GREMLIN_REMOTE_HOSTS=janusgraph -it janusblockchainclient ./bin/gremlin.sh
```

TODO

https://github.com/JanusGraph/janusgraph-docker/blob/master/docker-compose-cql-es.yml
