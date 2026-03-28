package protoactor;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import protoactor.AbstractProtoactor;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ProtoActorContext {
	
	private String name;
	private int port;
	private Javalin server = null;
	private Vector<WsConnectContext> allConns = new Vector<WsConnectContext>();
	private Map<String, AbstractProtoactor> protoactors = new ConcurrentHashMap<>();
	
	public ProtoActorContext(String name, int port) {
		this.name = name;
		this.port = port;
		configureTheSystem();		
	}
	
	public void register(AbstractProtoactor pactor) {
		protoactors.put(pactor.name, pactor);
		CommUtils.outgreen("registered " + pactor.name + " in " + name );
	}
	
	// --- CONFIGURAZIONE DEL SERVER ---
	
	protected void configureTheSystem() {  
		setUpServer(   );
	    setWorkWS( );   
	}
	
	protected void setUpServer(   ) {
		if( server == null ) server = Javalin.create(config -> {
	        config.jetty.modifyWebSocketServletFactory(factory -> {
	            factory.setIdleTimeout(java.time.Duration.ofMinutes(30)); 
	        });
	   }).start(port);
	}

	protected void setWorkWS( ) {
	
	  server.ws("/eval", 
		ws -> {  		            
			
		// Gestione delle connessioni WebSocket
		ws.onConnect( ctx -> {  
		 			allConns.add(ctx);
		 			System.out.println(name + " | ws: connection   " + "" + " Nconn=" + allConns.size()); 	             			 
		});
		
		// Gestione delle disconnessioni WebSocket
		ws.onClose( ctx -> {  
					allConns.remove(ctx);
					System.out.println(name + " | ws: disconnection" + "" + " Nconn=" + allConns.size()); 	             			 
		});
		
		// Gestione dei messaggi WebSocket
		ws.onMessage( ctx -> {  
					String msg = ctx.message();
					System.out.println(name + " | ws: message received:" + msg); 	             			 
		});
		
	  });
	}
	
	// Invia il messaggio a tutti i protoattori registrati
	public IApplMessage elabMsg(IApplMessage am, WsConnectContext ctx) {
    	CommUtils.outyellow(name + " elabMsg : " + am + " ctx null:" + (ctx==null)); 
		AbstractProtoactor p = protoactors.get(am.msgReceiver());
    	if( p != null )
			return p.execMsg(am);
		return null;
	}
	
	// Elaborazione dei messaggi ricevuti via WebSocket
	protected IApplMessage readInputWS(String message) {
		CommUtils.outyellow(name + " readInputWS : " + message);
		// Prova a convertire il messaggio in un oggetto IApplMessage
		try {
			IApplMessage am = new ApplMessage(message);
			return am;
		} catch (Exception e) {
			// Se la conversione fallisce, prova a interpretare il messaggio come JSON
			CommUtils.outred(name + " Errore nella conversione del messaggio: " + e.getMessage());
			try {
				IApplMessage am = ApplMessage.cvtJson(message);
				return am;
			} catch (Exception ex) {
				CommUtils.outred(name + " Errore nella conversione del messaggio JSON: " + ex.getMessage());
				IApplMessage ev = CommUtils.buildEvent("context","input",message );
				return ev;
			}
		}
	}
	
	protected void emitInfo(IApplMessage event) {
		//CommUtils.outcyan(name + " emitInfo " + s);
		// Invia a tutti i componenti esterni
		allConns.forEach( (conn) -> {
    		if( conn.session.isOpen() ) sendsafe(conn, event.toString()); 
    	});
		//Invio a tutti gli attori locali al contesto
		protoactors.forEach((id, pa) -> {
		    pa.execMsg( event );  
		});
	}
	
	protected void sendsafe(WsConnectContext ctx, String msg) {
		synchronized (ctx.session) { 
			if (ctx.session.isOpen()) {
				ctx.send(msg);
			}
		}
	}
	
}
