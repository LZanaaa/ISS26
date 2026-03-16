package conway.domain;

public interface IGrid {    
    
    // --- PRIMITIVE --- 
    int getRighe();  //Ottengo il numero naturale di righe della griglia  
    int getColonne(); //Ottengo il numero naturale di colonne della griglia
    ICell getCell(int r, int c);  //Ottengo la cella alla posizione riga r e colonna c della griglia

    // --- NON PRIMITIVE ---
    boolean getCellState(int r, int c); //Ottengo lo stato della cella alla posizione riga r e colonna c della griglia (viva o morta)
    void setCell(int r, int c, boolean stato); //Imposta lo stato di una cella (r,c).
    void reset(); //Riporta tutte le celle a stato morto 
    int countAliveNeighbours(int r, int c); //Conta il numero di vicini vivi della cella alla posizione riga r e colonna c della griglia
}