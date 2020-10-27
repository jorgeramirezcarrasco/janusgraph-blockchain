from gremlin_python import statics
from gremlin_python.structure.graph import Graph
from gremlin_python.process.traversal import Bindings
from gremlin_python.process.graph_traversal import __
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection

import csv

graph = Graph()
connection = DriverRemoteConnection('ws://localhost:8182/gremlin', 'g')
# The connection should be closed on shut down to close open connections with connection.close()
g = graph.traversal().withRemote(connection)
# Reuse 'g' across the application
print(g)

tsv_file = open('../data/blockchair_ethereum_transactions_20201025.tsv')
read_tsv = csv.DictReader(tsv_file, delimiter="\t")

for tx in read_tsv:
  print(tx['hash'])
  g.addV('tx').property('block_id',tx['block_id']).property('hash',tx['hash']).next()
  #Check it is stored
  print(g.V().has('tx','hash',tx['hash']).next())

'''
v1 = g.addV('person').property('name','marko').next()
v2 = g.addV('person').property('name','stephen').next()
g.V(Bindings.of('id',v1)).addE('knows').to(v2).property('weight',0.75).iterate()

marko = g.V().has('person','name','marko').next()
print(marko)
peopleMarkoKnows = g.V().has('person','name','marko').out('knows').toList()
print(peopleMarkoKnows)
'''