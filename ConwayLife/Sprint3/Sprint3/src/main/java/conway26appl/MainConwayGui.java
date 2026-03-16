public static void main(String[] args) {

    LifeInterface life = new Life(20, 20); 
    
    IOutDev webOutDev = new WebOutDev(); 
    
    GameController controller = new LifeController(life, webOutDev); 
    
    IoJavalin server = new IoJavalin();
    server.setup(controller); 
    
    System.out.println("MainConway | READY on http://localhost:8080");
}