package conway26appl;

import conway.io.IoJavalin;
import conway.domain.*;

public class MainConwayGui {
    public static void main(String[] args) {

        IoJavalin server = new IoJavalin();

        LifeInterface life = new Life(20, 20); 

        GameController controller = new LifeController(life, server);

        server.setup(controller);

        System.out.println("SISTEMA PRONTO: apri http://localhost:8080");
    }
}