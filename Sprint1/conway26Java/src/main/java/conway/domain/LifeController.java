package main.java.conway.controller;

import main.java.conway.domain.IGrid;

public class LifeController {
    
    private IGrid grid;

    public LifeController(IGrid grid) {
        this.grid = grid;
    }

    /**
     * Calcola la generazione successiva applicando le 4 regole di Conway.
     */
    public void tick() {
        int rows = grid.getRighe();
        int cols = grid.getColonne();
        
        // Matrice temporanea per non sporcare i dati durante il calcolo
        boolean[][] nextGen = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int vivi = countAliveNeighbors(r, c);
                boolean alive = grid.getCellStatus(r, c); //

                // Applichiamo le regole di Conway
                if (alive) {
                    // Sopravvive solo con 2 o 3 vicini vivi
                    nextGen[r][c] = (vivi == 2 || vivi == 3);
                } else {
                    // Nasce solo con esattamente 3 vicini vivi
                    nextGen[r][c] = (vivi == 3);
                }
            }
        }

        // Aggiorniamo la griglia originale con i nuovi stati
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid.setCellStatus(r, c, nextGen[r][c]);
            }
        }
    }

    /**
     * Conta i vicini vivi nell'intorno di Moore (8 celle)
     */
    private int countAliveNeighbors(int r, int c) {
        int count = 0;
        // Cicliamo da -1 a +1 rispetto alla cella centrale (r, c)
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                // Saltiamo la cella stessa (non è un vicino!)
                if (i == r && j == c) continue;

                // Controlliamo di non uscire dai bordi della griglia
                if (i >= 0 && i < grid.getRighe() && j >= 0 && j < grid.getColonne()) {
                    if (grid.getCellStatus(i, j)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}