# JanusGraph for BlockChain Use Case

Server (Using Cassandra and ElasticSearch)

```
docker-compose up
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
