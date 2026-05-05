%====================================================================================
% fireflysync description   
%====================================================================================
event( flash, flash(ID) ).
dispatch( info, changed(SOURCE,TERM) ). %inviata senza forward (con updateResource)
%====================================================================================
context(ctxfireflysync, "localhost",  "TCP", "8011").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( fireflyfactory, ctxfireflysync, "it.unibo.fireflyfactory.Fireflyfactory").
 static(fireflyfactory).
  qactor( firefly, ctxfireflysync, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
  qactor( sonar, ctxfireflysync, "it.unibo.sonar.Sonar").
 static(sonar).
