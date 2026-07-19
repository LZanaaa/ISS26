%====================================================================================
% fireflysync description   
%====================================================================================
event( flash, flash(ID) ).
dispatch( cellstate, cellstate(X,Y,S) ).
%====================================================================================
context(ctxfireflysync, "localhost",  "TCP", "8011").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( fireflyfactory, ctxfireflysync, "it.unibo.fireflyfactory.Fireflyfactory").
 static(fireflyfactory).
  qactor( firefly, ctxfireflysync, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
