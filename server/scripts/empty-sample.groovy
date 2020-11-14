def globals = [:]
ig = ConfiguredGraphFactory.open("my-demo-keyspace")
globals << [ig : ig.traversal()]