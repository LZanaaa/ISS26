package conway26appl;
import conway.domain.*;

public class MainGame {
    public static void main(String[] args) {
        System.out.println("=== AVVIO MOTORE DI GIOCO (CORE) ===");
        LifeInterface life = new Life(20, 20); 
        JavalinAdapter proxyOutDev = new JavalinAdapter();
        GameController controller = new LifeController(life, proxyOutDev);
        proxyOutDev.setController(controller);
        System.out.println("=== MOTORE PRONTO E IN ASCOLTO ===");
    }
}