package main.java.conway.domain;

public interface LifeInterface {
	
	// --- PRIMITIVE --- 
	IGrid getGrid(); //Ottengo la griglia di gioco
	void nextGeneration(); //Evolve la griglia di gioco secondo le regole del gioco della vita
	
	// --- NON PRIMITIVE ---
	void setCell(int r, int c, boolean stato); //Imposta lo stato di una cella (r,c).
	ICell getCell(int r, int c);  //Ottengo la cella alla posizione riga r e colonna c della griglia
	void reset(); //Riporta tutte le celle a stato morto
	boolean getCellState(int r, int c); //Ottengo lo stato della cella (r,c) della griglia (viva o morta)
	
	
}
