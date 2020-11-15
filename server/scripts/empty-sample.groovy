def globals = [:]
// defines a sample LifeCycleHook that prints some output to the Gremlin Server console.
// note that the name of the key in the "global" map is unimportant.
globals << [hook : [
        onStartUp: { ctx ->
            ctx.logger.info("Executed once at startup of Gremlin Server.")
        },
        onShutDown: { ctx ->
            ctx.logger.info("Executed once at shutdown of Gremlin Server.")
        }
] as LifeCycleHook]

Map map = new HashMap<String, Object>();
map.put("graph.graphname","ethereum") //database and keyspace name
map.put("ids.block-size",15000000) // no. of vertices expected to be inserted in an hour
map.put("query.batch",true) // bulk load activated
map.put("storage.backend","cql")
map.put("storage.cql.read-consistency-level","LOCAL_ONE") // read consistency level: (LOCAL_ONE, QUORUM...)
map.put("storage.cql.replication-factor",1) // replica of data across the cluster
map.put("storage.cql.write-consistency-level","LOCAL_ONE")  // write consistency level: (LOCAL_ONE, QUORUM...)
map.put("storage.hostname","cassandra") // IP or Hostname of host of storage
map.put("cluster.max-partitions",6)
map.put("storage.buffer-size", 2048) // No. of requests write batches for storage backend
map.put("ids.authority.wait-time", 1000)
ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map));

ethereum = g.openManagement()  //opens the management graph

//created vertex and edge labels
tx = ethereum.makeVertexLabel("TX").make()  
sender = ethereum.makeVertexLabel("SENDER").make()  
recipient = ethereum.makeVertexLabel("RECIPIENT").make()    
sent_by = ethereum.makeEdgeLabel("SENT_BY").make() 
received_by = ethereum.makeEdgeLabel("RECEIVED_BY").make()  

//creates properties data types
tx_id = ethereum.makePropertyKey("id").dataType(String.class).make() 

//link properties with vertex and edges
ethereum.addProperties(tx, tx_id)   

//build indexes for vertex and edges
ethereum.buildIndex('byTxid', Vertex.class).addKey(tx_id).indexOnly(tx).buildCompositeIndex()  

// prints the schema
ethereum.printSchema()   

//close resources
ethereum.commit()
ethereum.close()
g.close()

g = ConfiguredGraphFactory.open("test");
globals << [g : g.traversal()]