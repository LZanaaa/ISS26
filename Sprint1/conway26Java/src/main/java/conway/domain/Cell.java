package main.java.conway.domain;

public class Cell implements ICell{
	
	private boolean status;

	public Cell() {
		super();
	}
	
	@Override
	public boolean isAlive() {
		return status;
	}
	
	@Override
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	
	
}
