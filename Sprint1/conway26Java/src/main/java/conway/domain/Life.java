package main.java.conway.domain;

public class Life implements LifeInterface {
    private final int rows;
    private final int cols;

    // Ora usiamo la classe Grid invece degli array bidimensionali
    private Grid gridA;
    private Grid gridB;

    // Riferimenti che puntano alle griglie attuali
    private Grid currentGrid;
    private Grid nextGrid;

    public static LifeInterface CreateGameRules() {
        return new Life(5, 5);
    }

    // Costruttore che accetta una griglia pre-configurata (utile per i test)
    public Life(boolean[][] initialGrid) {
        this.rows = initialGrid.length;
        this.cols = initialGrid[0].length;

        // Inizializziamo le due griglie usando il costruttore Grid(dimx, dimy)
        this.gridA = new Grid(rows, cols);
        this.gridB = new Grid(rows, cols);

        // Copiamo i valori iniziali dentro gridA
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.gridA.setCell(r, c, initialGrid[r][c]);
            }
        }

        this.currentGrid = gridA;
        this.nextGrid = gridB;
    }

    // Costruttore che crea griglie vuote
    public Life(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.gridA = new Grid(rows, cols);
        this.gridB = new Grid(rows, cols);
        this.currentGrid = gridA;
        this.nextGrid = gridB;
    }

    // Calcola la generazione successiva applicando le 4 regole di Conway
    public void nextGeneration() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbors = countNeighborsLive(r, c);
                boolean isAlive = currentGrid.getCell(r, c); // Lettura da Grid
                
                // Applichiamo le regole
                if (isAlive) {
                    nextGrid.setCell(r, c, (neighbors == 2 || neighbors == 3));
                } else {
                    nextGrid.setCell(r, c, (neighbors == 3));
                }
            }
        }

        // --- IL PING-PONG ---
        // Scambiamo i riferimenti degli oggetti Grid
        Grid temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }

    protected int countNeighborsLive(int row, int col) {
        int count = 0;
        if (row - 1 >= 0) {
            if (currentGrid.getCell(row - 1, col)) count++;
        }
        if (row - 1 >= 0 && col - 1 >= 0) {
            if (currentGrid.getCell(row - 1, col - 1)) count++;
        }
        if (row - 1 >= 0 && col + 1 < cols) {
            if (currentGrid.getCell(row - 1, col + 1)) count++;
        }
        if (col - 1 >= 0) {
            if (currentGrid.getCell(row, col - 1)) count++;
        }
        if (col + 1 < cols) {
            if (currentGrid.getCell(row, col + 1)) count++;
        }
        if (row + 1 < rows) {
            if (currentGrid.getCell(row + 1, col)) count++;
        }
        if (row + 1 < rows && col - 1 >= 0) {
            if (currentGrid.getCell(row + 1, col - 1)) count++;
        }
        if (row + 1 < rows && col + 1 < cols) {
            if (currentGrid.getCell(row + 1, col + 1)) count++;
        }
        return count;
    }

    // Metodi di utilità per i test (aggiornati per usare Grid)
    public boolean getCell(int r, int c) {
        return currentGrid.getCell(r, c);
    }

    public void setCell(int r, int c, boolean state) {
        currentGrid.setCell(r, c, state);
    }

    public Grid getGrid() {
        return currentGrid;
    }

    @Override
    public boolean isAlive(int row, int col) {
        return currentGrid.getCell(row, col);
    }

    @Override
    public int getRows() {
        return this.rows; // Corretto: prima tornava 0!
    }

    @Override
    public int getCols() {
        return this.cols; // Corretto: prima tornava 0!
    }

    // Aggiornato per leggere dalla Grid invece che fare stream su array primitivi
    public String gridRep() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(currentGrid.getCell(r, c) ? "O " : ". ");
            }
            if (r < rows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}