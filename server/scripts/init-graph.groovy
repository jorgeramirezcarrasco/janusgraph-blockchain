map = new HashMap();
map.put("graph.graphname","my-demo-keyspace")
map.put("ids.block-size",15000000)
map.put("query.batch",true)
map.put("storage.backend","cql")
map.put("storage.cql.read-consistency-level","LOCAL_ONE")
map.put("storage.cql.replication-factor",3)
map.put("storage.cql.write-consistency-level","LOCAL_ONE")
map.put("storage.hostname","cassandra")
map.put("cluster.max-partitions",6)
map.put("storage.buffer-size", 2048)
map.put("ids.authority.wait-time", 1000)
ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map))

mgmt = g1.openManagement()  #opens the management graph

cookie = mgmt.makeVertexLabel("COOKIE").make()  
mobile = mgmt.makeVertexLabel("MOBILE").make()  
email = mgmt.makeVertexLabel("EMAIL").make()    
rel = mgmt.makeEdgeLabel("REL").make()   

idvalue = mgmt.makePropertyKey("idValue").dataType(String.class).make() 

idtype = mgmt.makePropertyKey("idType").dataType(String.class).make()  
timestamp = mgmt.makePropertyKey("timestamp").dataType(Long.class).make()  
edgeValue = mgmt.makePropertyKey("edgeValue").dataType(String.class).make()  

mgmt.addProperties(rel, edgeValue, timestamp)   
mgmt.addProperties(cookie, idvalue, idtype, timestamp)
mgmt.addProperties(mobile, idvalue, idtype, timestamp)
mgmt.addProperties(email, idvalue, idtype, timestamp)

mgmt.buildIndex('byCookiedValueComposite', Vertex.class).addKey(idvalue).indexOnly(cookie).buildCompositeIndex()  
mgmt.buildIndex('byMobileidValueComposite', Vertex.class).addKey(idvalue).indexOnly(mobile).buildCompositeIndex()
mgmt.buildIndex('byEmailidValueComposite', Vertex.class).addKey(idvalue).indexOnly(email).buildCompositeIndex()
mgmt.buildIndex('byRelEdgeValueComposite', Edge.class).addKey(edgeValue).indexOnly(rel).buildCompositeIndex()

mgmt.printSchema()   

mgmt.commit()
mgmt.close()
g.close()