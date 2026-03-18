package conway.domain;

public class Grid implements IGrid {
    
    private int righe;
    private int colonne;
    
    private ICell[][] celle; 

    public Grid(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
        
        this.celle = new Cell[righe][colonne]; 
        
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                celle[i][j] = new Cell();
            }
        }
    }
    

    public boolean getCellState(int r, int c) {
        return celle[r][c].isAlive();
    }

    public void setCell(int r, int c, boolean stato) {
        celle[r][c].setStatus(stato);
    }

    public int getRighe() {
        return righe;
    }

    public int getColonne() {
        return colonne;
    }


	@Override
	public ICell getCell(int r, int c) {
		
		return celle[r][c];
		
	}


	@Override
	public void reset() {

		for (int i = 0; i < righe; i++) {
			for (int j = 0; j < colonne; j++) {
				celle[i][j].setStatus(false);
			}
		}
		
	}
	
	@Override
	public int countAliveNeighbours(int r, int c) {
		int count = 0;
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue; // Salta la cella stessa
				int neighbourRow = r + i;
				int neighbourCol = c + j;
				if (neighbourRow >= 0 && neighbourRow < righe && neighbourCol >= 0 && neighbourCol < colonne) {
					if (celle[neighbourRow][neighbourCol].isAlive()) {
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
	
}