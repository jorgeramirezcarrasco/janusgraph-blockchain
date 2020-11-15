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
map.put("graph.graphname","test")
map.put("ids.block-size",15000000)
map.put("query.batch",true)
map.put("storage.backend","cql")
map.put("storage.cql.read-consistency-level","LOCAL_ONE")
map.put("storage.cql.replication-factor",1)
map.put("storage.cql.write-consistency-level","LOCAL_ONE")
map.put("storage.hostname","cassandra")
map.put("cluster.max-partitions",6)
map.put("storage.buffer-size", 2048)
map.put("ids.authority.wait-time", 1000)
ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map));
g = ConfiguredGraphFactory.open("test");
globals << [g : g.traversal()]