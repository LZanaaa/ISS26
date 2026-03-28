package protoactor;

import io.javalin.Javalin;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class MainSistemaSPactor {

    public static void main(String[] args) {
    	
        // --- Init del contesto e dell'attore ---
        ProtoActorContext ctx = new ProtoActorContext("mainContext", 8010);
        CalcActor calcActor = new CalcActor("calcActor", ctx);

        // --- Avvio del server Javalin ---
        Javalin app = Javalin.create(config -> {
        }).start(8080);

        CommUtils.outmagenta("Javalin (H) | Server Javalin avviato sulla porta 8080");
        
        
        // --- CANALE HTTP ---
        app.get("/calc", webCtx -> {
            String xValue = webCtx.queryParam("x");

            if (xValue == null || xValue.isEmpty()) {
                webCtx.status(400).result("Errore: parametro 'x' mancante.");
                return;
            }

            CommUtils.outblue("Javalin (HTTP) | Ricevuto x = " + xValue);

            IApplMessage reqMsg = CommUtils.buildRequest("javalinHttp", "calcRequest", xValue, "calcActor");

            IApplMessage replyMsg = calcActor.execMsg(reqMsg);

            if (replyMsg != null) {
                webCtx.status(200).result("Risultato f(" + xValue + ") = " + replyMsg.msgContent());
            } else {
                webCtx.status(500).result("Errore nel calcolo.");
            }
        });

        // --- WEBSOCKET ---
        app.ws("/ws-calc", ws -> {
            ws.onConnect(webCtx -> CommUtils.outgreen("Javalin (WS) | Client connesso."));
            
            ws.onMessage(webCtx -> {
                String xValue = webCtx.message(); 
                CommUtils.outblue("Javalin (WS) | Ricevuto messaggio: " + xValue);

                IApplMessage reqMsg = CommUtils.buildRequest("javalinWs", "calcRequest", xValue, "calcActor");
                IApplMessage replyMsg = calcActor.execMsg(reqMsg);

                if (replyMsg != null) {
                    webCtx.send("Risultato: " + replyMsg.msgContent());
                } else {
                    webCtx.send("Errore nel calcolo.");
                }
            });
            
            ws.onClose(webCtx -> CommUtils.outred("Javalin (WS) | Client disconnesso."));
        });
    }
}