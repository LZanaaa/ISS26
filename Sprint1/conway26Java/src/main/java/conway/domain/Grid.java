package main.java.conway.domain;

public class Grid implements IGrid {
    
    private int righe;
    private int colonne;
    
    private boolean[][] celle; 

    public Grid(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
        this.celle = new boolean[righe][colonne]; 
    }
    

    public boolean getCellState(int r, int c) {
        return celle[r][c];
    }

    public void setCell(int r, int c, boolean stato) {
        celle[r][c] = stato;
    }

    public int getRighe() {
        return righe;
    }

    public int getColonne() {
        return colonne;
    }
}