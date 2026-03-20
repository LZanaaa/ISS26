package conway26appl;

import conway.domain.GameController;
import conway.domain.IGrid;
import conway.domain.IOutDev;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;
import java.util.Observable;

public class JavalinAdapter implements IOutDev, IObserver {
    
    private Interaction conn;
    private GameController controller;

    public JavalinAdapter() {
        try {
            CommUtils.outgreen("Motore | Connessione al Server Javalin in corso...");
            
            conn = WsConnection.create("localhost:8080", "eval", this);
            
            conn.forward("SYSTEM:ENGINE_READY"); 
            
            CommUtils.outgreen("Motore | Connesso al Server con successo!");
        } catch (Exception e) {
            CommUtils.outred("Motore | ERRORE: Impossibile connettersi a Javalin. Hai avviato MainServer?");
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void displayGrid(IGrid grid) {
        try {
            for (int i = 0; i < grid.getRighe(); i++) {
                for (int j = 0; j < grid.getColonne(); j++) {
                    // 0 = vivo, 1 = morto
                    int stato = grid.getCellState(i, j) ? 0 : 1;
                    // Invece di stampare a video, spedisce la cella!
                    conn.forward("cell(" + i + "," + j + "," + stato + ")");
                }
            }
        } catch (Exception e) {
            CommUtils.outred("Motore | Errore invio griglia");
        }
    }

    @Override
    public void displayCell(IGrid grid, int x, int y) {}
    @Override
    public void display(String msg) {}
    @Override
    public void close() {}


    @Override
    public void update(String value) {
        CommUtils.outblue("Motore | Comando ricevuto dall'Owner: " + value);
        try {
            if (value.contains("cell(")) {
                String clean = value.replace("cell(", "").replace(")", "").trim();
                String[] parts = clean.split(",");
                controller.switchCellStatus(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } 
            else if (value.equals("start")) { controller.onStart(); } 
            else if (value.equals("stop"))  { controller.onStop(); } 
            else if (value.equals("clear")) { controller.onClear(); }
        } catch (Exception e) {
            CommUtils.outred("Motore | Comando non valido.");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        update(arg.toString());
    }
}