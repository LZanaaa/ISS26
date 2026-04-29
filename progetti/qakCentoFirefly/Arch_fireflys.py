### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('fireflysArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxfirefly', graph_attr=nodeattr):
          fireflyfactory=Custom('fireflyfactory','./qakicons/symActorWithobjSmall.png')
          firefly=Custom('firefly','./qakicons/symActorDynamicWithobj.png')
          mockobserver=Custom('mockobserver','./qakicons/symActorWithobjSmall.png')
     firefly >> Edge(color='blue', style='solid',  decorate='true', label='<flash &nbsp; >',  fontcolor='blue') >> mockobserver
diag
