package javalin;

import io.javalin.Javalin;
import domain.MathUtils;

public class SistemaSJavalin {

    public void doJob() {

        Javalin app = Javalin.create().start(8080);
        
        System.out.println("=== Server avviato sulla porta 8080 ===");

        // --- CANALE WEBSOCKET ---
        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Nuova connessione WS: " + ctx.sessionId());
                ctx.send("Connessione stabilita. Invia un numero per il calcolo.");    
            });
            
            ws.onMessage(ctx -> {
                String messaggio = ctx.message();
                System.out.println("Ricevuto via WS: " + messaggio);
                try {
                    double x = Double.parseDouble(messaggio);
                    double risultato = MathUtils.calculate(x);
                    ctx.send("Risultato: " + risultato);
                } catch (Exception e) {
                    ctx.send("Errore: nuemro invalido.");
                }
            });
            
            ws.onClose(ctx -> {
                System.out.println("Connessione WS chiusa: " + ctx.sessionId());
            });
            
            ws.onError(ctx -> {
                System.out.println("Errore WebSocket: " + ctx.error().getMessage());
            });
        });
        
        // --- CANALE HTTP ---
        app.get("/calculate", ctx -> {
        	
            String xStr = ctx.queryParam("x");
            
            if (xStr == null) {
                ctx.status(400).result("Errore: parametro 'x' mancante nell'URL.");
                return;
            }
            
            try {
                double x = Double.parseDouble(xStr);
                double risultato = MathUtils.calculate(x);
                ctx.result("Risultato: " + risultato);
            } catch (NumberFormatException e) {
                ctx.status(400).result("Errore: il valore '" + xStr + "' non è un numero valido.");
            }
        });
    }

    public static void main(String[] args) {

        new SistemaSJavalin().doJob();
    }
}