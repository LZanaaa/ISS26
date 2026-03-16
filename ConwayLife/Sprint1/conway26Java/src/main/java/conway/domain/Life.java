package conway.domain;

public class Life implements LifeInterface {
	
	IGrid grid;
	int righe;
	int colonne;
	
	public Life(int righe, int colonne) {
		this.righe = righe;
		this.colonne = colonne;
		grid = new Grid(righe, colonne);
	}

	@Override
	public IGrid getGrid() {
		
		return grid;
		
	}

	@Override
	public void nextGeneration() {
		
		int righe = grid.getRighe();
		int colonne = grid.getColonne();
		
		IGrid tempGrid = new Grid(righe, colonne);
		
		 for (int i = 0; i < righe; i++) {
	            for (int j = 0; j < colonne; j++) {
	            	int aliveNeighbours = grid.countAliveNeighbours(i, j);
	            	boolean currentState = grid.getCellState(i, j);
	            	
	            	if(currentState && (aliveNeighbours < 2 || aliveNeighbours > 3)) {
	            		tempGrid.setCell(i, j, false); 
	            	} else if (!currentState && aliveNeighbours == 3) {
	            		tempGrid.setCell(i, j, true); 
	            	} else {
	            		tempGrid.setCell(i, j, currentState); 
	            	}
	                
	            }
	     }
		 
		 for (int i = 0; i < righe; i++) {
	            for (int j = 0; j < colonne; j++) {
	                grid.setCell(i, j, tempGrid.getCellState(i, j));
	            }
	     }
		 
		
	}

	@Override
	public void setCell(int r, int c, boolean stato) {
		
		grid.setCell(r, c, stato);
		
	}

	@Override
	public ICell getCell(int r, int c) {
		
		return grid.getCell(r, c);
	}

	@Override
	public void reset() {
		grid.reset();
		
	}

	@Override
	public boolean getCellState(int r, int c) {
		return grid.getCellState(r, c);
	}

}
