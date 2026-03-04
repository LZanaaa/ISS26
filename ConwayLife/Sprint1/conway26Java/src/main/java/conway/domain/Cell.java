package main.java.conway.domain;

public class Cell implements ICell{
	
	private boolean status;

	public Cell() {
		super();
		this.status = false; // Inizialmente, la cella è morta
	}
	
	@Override
	public boolean isAlive() {
		return status;
	}
	
	@Override
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public void switchCellStatus() {
		this.status = !this.status; 
	}
	
	
	
}
