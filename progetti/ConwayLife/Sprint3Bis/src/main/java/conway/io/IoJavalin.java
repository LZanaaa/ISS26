package conway.io;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class IoJavalin { 
	
	// Lista dei browser web connessi
	private Set<WsContext> connectedClients = ConcurrentHashMap.newKeySet(); 
	private WsContext ownerCtx = null; 
	
	// Connessione speciale dedicata SOLO al Motore di Gioco (Core)
	private WsContext engineCtx = null; 

	public IoJavalin() {
		
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH;
		    });
		}).start(8080);
 
        // givva la pagin html
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
         * WEBSOCKKET 
         */
        app.ws("/eval", ws -> {
            
        	ws.onConnect(ctx -> {
                CommUtils.outgreen("IoJavalin | Nuovo socket connesso: " + ctx.sessionId());
            });
        	
            ws.onMessage(ctx -> {
                String message = ctx.message();     
                
                try {
                	
                    String msgContent = message;
                    try {
                        IApplMessage m = new ApplMessage(message);
                        msgContent = m.msgContent();
                    } catch (Exception e) {
                        
                    }
                    
                    if (msgContent.equals("SYSTEM:ENGINE_READY")) {
                    	engineCtx = ctx;
                    	CommUtils.outmagenta("IoJavalin | MOTORE DI GIOCO CONNESSO!");
                    	return;
                    }
                    
                    if (engineCtx != null && ctx.sessionId().equals(engineCtx.sessionId())) {
                    	for (WsContext browser : connectedClients) {
                			browser.send(msgContent); // Inoltra a tutti i browser
                		}
                    	return;
                    }
                    
                    if (msgContent.equals("ready")) { 
                    	connectedClients.add(ctx);
                    	if (ownerCtx == null) {
                        	ownerCtx = ctx;
                        	ctx.send("ROLE:OWNER");
                        } else {
                        	ctx.send("ROLE:OBSERVER");
                        }
                    	return;
                    }
                    
                    //Check owner
                    if (ownerCtx == null || !ctx.sessionId().equals(ownerCtx.sessionId())) {
                    	CommUtils.outred("IoJavalin | Rifiutato comando da OBSERVER");
                    	return;
                    }
                    
                    if (engineCtx != null) {
                    	CommUtils.outcyan("IoJavalin | Inoltro comando '" + msgContent + "' al Motore.");
                    	engineCtx.send(msgContent);
                    } else {
                    	CommUtils.outred("IoJavalin | ERRORE: L'Owner ha inviato un comando, ma il Motore è SPENTO!");
                    }
                    
                } catch(Throwable e) {
                	CommUtils.outred("Javalin | Errore generico: " + e.getMessage());
                }               
            });
            
            ws.onClose(ctx -> {
            	connectedClients.remove(ctx);

            	if (engineCtx != null && ctx.sessionId().equals(engineCtx.sessionId())) {
            		CommUtils.outred("IoJavalin | ATTENZIONE: Il Motore di Gioco si è disconnesso!");
            		engineCtx = null;
            	} 
            	else if (ownerCtx != null && ctx.sessionId().equals(ownerCtx.sessionId())) {
            		CommUtils.outred("IoJavalin | L'OWNER si è disconnesso!");
            		ownerCtx = null;
            		if (!connectedClients.isEmpty()) {
            			ownerCtx = connectedClients.iterator().next();
            			ownerCtx.send("ROLE:OWNER");
            		}
            	}
            });
        });        
	} 
} 