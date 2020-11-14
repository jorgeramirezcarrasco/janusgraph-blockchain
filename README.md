# JanusGraph for BlockChain Use Case

Server (Using default configuration for Gremlin Server, Cassandra and ElasticSearch)

```
docker-compose up
```

Server (Using ConfiguredGraphFactory for Bulk Update in Gremlin Server, Cassandra and ElasticSearch)

```
docker-compose -f docker-compose-bulk.yaml build

docker-compose -f docker-compose-bulk.yaml up
```

Client

```
python3.7 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python janus_client.py
```

Add Graph Explorer to Visualize Graph

```
docker run -p 8888:8888 -d --name graph-explorer invanalabs/graph-explorer
```

In the browser connect using: ws://localhost:8182/gremlin
