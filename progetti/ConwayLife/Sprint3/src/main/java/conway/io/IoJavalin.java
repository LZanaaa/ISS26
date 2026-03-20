package conway.io;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import conway.domain.*;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class IoJavalin implements IOutDev {
	
	private Set<WsContext> connectedClients = ConcurrentHashMap.newKeySet();
	private WsContext ownerCtx = null; 
	
	private GameController controller;

	public IoJavalin() {
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH;
		    });
		}).start(8080);
        
         app.get("/", ctx -> {
        	var inputStream = getClass().getResourceAsStream("/page/ConwayInOutPage.html");       	
        	if (inputStream != null) {
        	    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        	    ctx.html(content);
        	} else {
		        ctx.status(404).result("File HTML non trovato");
		    }
        }); 
        

        /*
         * --------------------------------------------
         * Websocket
         * --------------------------------------------
         */
        app.ws("/eval", ws -> {
            
        	// --- NUOVA CONNESSIONE ---
        	ws.onConnect(ctx -> {
                CommUtils.outgreen("IoJavalin | Nuovo client connesso: " + ctx.sessionId());
                connectedClients.add(ctx);
                
                //Il primo è owner
                if (ownerCtx == null) {
                	ownerCtx = ctx;
                	ctx.send("ROLE:OWNER"); 
                	CommUtils.outgreen("IoJavalin | Assegnato ruolo OWNER a: " + ctx.sessionId());
                } else {
                	ctx.send("ROLE:OBSERVER"); 
                	CommUtils.outcyan("IoJavalin | Assegnato ruolo OBSERVER a: " + ctx.sessionId());
                }
            });
        	
        	//Ricezione messaggi
            ws.onMessage(ctx -> {
                String message = ctx.message();     
                CommUtils.outblue("IoJavalin | eval riceve: " + message);
                
                try {
                	IApplMessage m = new ApplMessage(message);
                    String msgContent = m.msgContent();
                    
                    
                    if (msgContent.equals("ready")) { 
                    	return;
                    }
                    
                    
                    if (ownerCtx == null || !ctx.sessionId().equals(ownerCtx.sessionId())) {
                    	CommUtils.outred("IoJavalin | Rifiutato comando '" + msgContent + "' da OBSERVER (" + ctx.sessionId() + ")");
                    	return; 
                    }
                    
                    // Se arriviamo qui, il mittente è l'OWNER. Eseguiamo i comandi:
                    if(msgContent.contains("cell(")) { 
                    	String[] parts = msgContent.replace("cell(", "").replace(")", "").trim().split(",");
                    	int r = Integer.parseInt(parts[0].trim());
                    	int c = Integer.parseInt(parts[1].trim());
                    	if(controller != null) controller.switchCellStatus(r, c);
                    } else if(msgContent.equals("start")) { 
						if(controller != null) controller.onStart();
					} else if(msgContent.equals("stop")) {
						if(controller != null) controller.onStop();
					} else if(msgContent.equals("clear")) {
						if(controller != null) controller.onClear();
					}                    
                } catch(Throwable e) {
                	CommUtils.outred("Javalin | Errore nel parsing del messaggio: " + message);
                    e.printStackTrace();
                }               
            });
            
            // 4. DISCONNESSIONE
            ws.onClose(ctx -> {
            	CommUtils.outgreen("IoJavalin | Client disconnesso: " + ctx.sessionId());
            	connectedClients.remove(ctx);
            	
            	
            	if (ctx == ownerCtx) {
            		CommUtils.outred("IoJavalin | L'OWNER si è disconnesso!");
            		ownerCtx = null;
            		
            		// Promuovere un altro Observer a Owner
            		if (!connectedClients.isEmpty()) {
            			ownerCtx = connectedClients.iterator().next();
            			ownerCtx.send("ROLE:OWNER");
            			CommUtils.outgreen("IoJavalin | Nuovo OWNER promosso: " + ownerCtx.sessionId());
            		}
            	}
            });
        });        
	}
	
	public void setup(GameController controller) {
	    this.controller = controller;
	}

	@Override
	public void display(String msg) {
		for (WsContext ctx : connectedClients) {
			ctx.send(msg);
		}
	}

	@Override
	public void close() {
		for (WsContext ctx : connectedClients) {
			ctx.send("close");
		}
	}

	@Override
	public void displayGrid(IGrid grid) {
		if (connectedClients.isEmpty()) return;
		
        for (int i = 0; i < grid.getRighe(); i++) {
            for (int j = 0; j < grid.getColonne(); j++) {
                int stato = grid.getCellState(i, j) ? 0 : 1;
                String cellMsg = "cell(" + i + "," + j + "," + stato + ")";
                
                for (WsContext ctx : connectedClients) {
                	ctx.send(cellMsg);
                }
            }
        }
	}
	
	@Override
	public void displayCell(IGrid grid, int x, int y) {
		if (connectedClients.isEmpty()) return;
		
        int stato = grid.getCellState(x, y) ? 0 : 1;
        String cellMsg = "cell(" + x + "," + y + "," + stato + ")";
        
        // Invia l'aggiornamento a tutti i client
        for (WsContext ctx : connectedClients) {
        	ctx.send(cellMsg);
        }
	}
}