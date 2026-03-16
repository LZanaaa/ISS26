package conway.io;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsMessageContext;
import conway.domain.*;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class IoJavalin implements IOutDev {
	
	private WsMessageContext pageCtx ;
	private GameController controller;

	
	public IoJavalin() {
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido esterno.
				 */
		    });
		}).start(8080);
 
/*
 * --------------------------------------------
 * Parte HTTP        
 * --------------------------------------------
 */
        app.get("/", ctx -> {
    		//Path path = Path.of("./src/main/resources/page/ConwayInOutPage.html");    		    
        	/*
        	 * Java cercherà il file all'interno del Classpath 
        	 * (dentro il JAR o nelle cartelle dei sorgenti di Eclipse), 
        	 * rendendo il codice universale
         	 */
        	var inputStream = getClass().getResourceAsStream("/page/ConwayInOutPage.html");       	
        	if (inputStream != null) {
        		// Trasformiamo l'inputStream in stringa (o lo mandiamo come stream)
        	    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        	    ctx.html(content);
        	} else {
		        ctx.status(404).result("File non trovato nel file system");
		    }
		    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
        }); 
        
        app.get("/greet/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name + "!");
        }); //http://localhost:8080/greet/Alice
        
        app.get("/api/users", ctx -> {
            Map<String, Object> user = Map.of("id", 1, "name", "Bob");
            ctx.json(user); // Auto-converts to JSON
        });
        
        /*
         * Javalin v5+: Si passa solo la "promessa" (il Supplier del Future). 
         * Javalin è diventato più intelligente: se il Future restituisce una Stringa, 
         * lui fa ctx.result(stringa). Se restituisce un oggetto, lui fa ctx.json(oggetto).
         * 
         */
        app.get("/async", ctx -> {
        	ctx.future(() -> {
	        	// Creiamo il future
	            CompletableFuture<String> future = new CompletableFuture<>();
	            
	            // Eseguiamo il lavoro in un altro thread
	            new Thread(() -> { 
	                try {
	                    Thread.sleep(2000); // Simulazione calcolo pesante
	                    future.complete("IoJavalin | Risultato calcolato asincronamente");
	                } catch (Exception e) {
	                    future.completeExceptionally(e);
	                }
	            });
	            
	            return future; // Restituiamo il future a Javalin
        	});
        });
        
        app.get("/async1", ctx -> {
            ctx.future(() -> CompletableFuture.supplyAsync(() -> {
                // Simuliamo l'operazione lenta
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "IoJavalin | Risultato calcolato con supplyAsync";
            }));
        });
/*
 * --------------------------------------------
 * Parte Websocket
 * --------------------------------------------
 */
        
        app.ws("/chat", ws -> {
            ws.onConnect(ctx -> CommUtils.outgreen("Client connected chat!"));
            ws.onMessage(ctx -> {
                String message = ctx.message();
                CommUtils.outcyan("IoJavalin |  riceve:" + message);
                ctx.send("Echo: " + message);
            });
        });        
        app.ws("/eval", ws -> {
            ws.onConnect(ctx -> CommUtils.outgreen("IoJavalin | Client connected eval"));
            ws.onMessage(ctx -> {
                String message = ctx.message();     
                CommUtils.outblue("IoJavalin |  eval receives:" + message );
                try {
                	IApplMessage m = new ApplMessage(message);
                    CommUtils.outblue("IoJavalin |  eval:" + m.msgContent() );
                    if( m.msgContent().equals("ready")) { 
                    	pageCtx = ctx;  //memorizzo connession pagina
                    }else if( m.msgContent().contains("cell(")) { 
                    	
                    	String[] parts = m.msgContent().replace("cell(", "").replace(")", "").trim().split(",");
                    	
                    	int r = Integer.parseInt(parts[0].trim());
                    	int c = Integer.parseInt(parts[1].trim());
                    	
                    	if(controller != null) controller.switchCellStatus(r, c);
                    	
                    }else if( m.msgContent().equals("start")) { 
						if(controller != null) controller.onStart();
					}else if( m.msgContent().equals("stop")) {
						if(controller != null) controller.onStop();
					}else if( m.msgContent().equals("clear")) {
						if(controller != null) controller.onClear();
					}                    
                }catch(Throwable e) {
                	CommUtils.outred("Javalin | Errore nel parsing del messaggio: " + message);
                    e.printStackTrace();
                }               
            });
        });        
	}
	
 
	public void setup(GameController controller) {
	    this.controller = controller;
	}



	@Override
	public void display(String msg) {
		 if (pageCtx != null) {
			 pageCtx.send(msg);
		 } else {
			 CommUtils.outred("IoJavalin | display: Nessun client connesso per inviare il messaggio: " + msg);
		 }
		
	}

	@Override
	public void close() {
		 if (pageCtx != null) {
			 pageCtx.send("close");
		 } else {
			 CommUtils.outred("IoJavalin | close: Nessun client connesso per inviare il messaggio di chiusura");
		 }
		
	}


	@Override
	public void displayGrid(IGrid grid) {
	    if (pageCtx != null) {

	        for (int i = 0; i < grid.getRighe(); i++) {
	            for (int j = 0; j < grid.getColonne(); j++) {
	                int stato = grid.getCellState(i, j) ? 1 : 0;
	                pageCtx.send("cell(" + i + "," + j + "," + stato + ")");
	            }
	        }
	    } else {
	        CommUtils.outred("IoJavalin | displayGrid: Nessun client connesso");
	    }
	}
	
	@Override
	public void displayCell(IGrid grid, int x, int y) {
	    if (pageCtx != null) {
	        int stato = grid.getCellState(x, y) ? 1 : 0;
	        pageCtx.send("cell(" + x + "," + y + "," + stato + ")");
	    }
	}

}
