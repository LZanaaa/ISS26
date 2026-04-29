%====================================================================================
% fireflys description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,S) ).
event( flash, flash(id) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8011").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( fireflyfactory, ctxfirefly, "it.unibo.fireflyfactory.Fireflyfactory").
 static(fireflyfactory).
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
