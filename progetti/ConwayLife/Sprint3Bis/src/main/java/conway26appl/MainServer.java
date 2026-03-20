package conway26appl;
import conway.io.IoJavalin;

public class MainServer {
    public static void main(String[] args) {
        System.out.println("=== Avvio Server Javalin (Esterno) ===");
        new IoJavalin(); 
        System.out.println("=== SERVER PRONTO su porta 8080 ===");
    }
}