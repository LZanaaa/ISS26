%====================================================================================
% fireflys description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,COLOR) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8011").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "external").
  qactor( fireflys, ctxfirefly, "it.unibo.fireflys.Fireflys").
 static(fireflys).
