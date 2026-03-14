package main.java.conway.domain;

import unibo.basicomm23.utils.CommUtils;


public class LifeController implements GameController {
    
    private int generationTime = 500;
    private LifeInterface life;
    private IOutDev outdev; 
    protected boolean running = false;
    protected int epoch = 0;

    public LifeController(LifeInterface game, IOutDev outdev) {  
        this.life = game;       
        this.outdev = outdev;
        CommUtils.outyellow("LifeController CREATED outdev=" + outdev);
    }
    
    @Override
    public int getGenTime() {
        return generationTime;
    }
 
    /*
     * Funzioni di controllo del gioco
     */
    
    protected void startTheGame() {
        if (running) return; // se è già partito, ignora il comando
        running = true;
        epoch = 0;
        play();		
    }
	
    protected void stopTheGame() {
        running = false;		
    }

    protected void clearTheGame() {
        if (outdev != null) outdev.display("lfctrl: clearing");
        stopTheGame();
        epoch = 0;
        resetAndDisplayGrids();   
    }
	
    protected void printout(String s) {
        if (outdev != null) outdev.display(s);
    }
	
    protected void play() {  
        // Avviamo un Thread separato per non bloccare la ricezione di altri comandi
        new Thread() {
            public void run() {			
                if (outdev != null) outdev.displayGrid(life.getGrid()); 
                
                while (running) {
                    // Pausa tra una generazione e l'altra
                    CommUtils.delay(generationTime);
                    
                    // Il nostro motore macina la generazione successiva
                    life.nextGeneration();
                    
                    if (outdev != null) outdev.displayGrid(life.getGrid());
                    epoch++;
                }
                printout("gamestopped"); 
            }
        }.start();
    }

    protected void resetAndDisplayGrids() {
        // ADATTAMENTO 2: Il nostro metodo per pulire si chiama reset(), non resetGrids()
        life.reset();
        
        if (outdev != null) {
            outdev.displayGrid(life.getGrid());
        }
    }
	
    @Override
    public void onStart() {
        startTheGame();	
    }

    @Override
    public void onStop() {
        stopTheGame();	
    }

    @Override
    public void onClear() {
        clearTheGame();	
    }
    
    @Override
    public int numEpoch() {
        return epoch;
    }

	@Override
	public void switchCellStatus(int x, int y) {
		switchCellStatus(x, y);
	}


}