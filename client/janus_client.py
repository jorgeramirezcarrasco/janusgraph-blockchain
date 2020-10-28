from gremlin_python import statics
from gremlin_python.structure.graph import Graph
from gremlin_python.process.traversal import Bindings
from gremlin_python.process.graph_traversal import __
from gremlin_python.driver.driver_remote_connection import DriverRemoteConnection

import csv

graph = Graph()
connection = DriverRemoteConnection('ws://localhost:8182/gremlin', 'g')

g = graph.traversal().withRemote(connection)

tsv_file = open('../data/blockchair_ethereum_transactions_20201025.tsv')
read_tsv = csv.DictReader(tsv_file, delimiter="\t")

for tx in read_tsv:
  tx = g.addV('tx')
  for key in tx.keys:
    if key == "sender":
      sender = g.addV('sender').next()
    if key == "receiver":
      receiver = g.addV('receiver').next()
    tx.property(key,tx[key])
  tx.next()
  #Check if it is stored
  print(g.V().has('tx','hash',tx['hash']).next())
  # Create links
  g.V(Bindings.of('id',tx)).addE('sent by').to(sender).iterate()
  g.V(Bindings.of('id',tx)).addE('received by').to(receiver).iterate()

connection.close()