package main.java.conway.domain;

public interface IGrid {
    
    int getRighe();
    
    int getColonne();
    
    boolean getCellState(int r, int c);
    
    ICell getCell(int r, int c);
    
    void setCell(int r, int c, boolean stato);
    
    void clear();
    
}