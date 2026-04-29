%====================================================================================
% fireflys description   
%====================================================================================
dispatch( flash, arg(X,Y,S) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8011").
 qactor( fireflyfactory, ctxfirefly, "it.unibo.fireflyfactory.Fireflyfactory").
 static(fireflyfactory).
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
  qactor( mockobserver, ctxfirefly, "it.unibo.mockobserver.Mockobserver").
 static(mockobserver).
