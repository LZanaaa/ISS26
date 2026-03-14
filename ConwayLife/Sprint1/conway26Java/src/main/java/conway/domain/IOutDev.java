package main.java.conway.domain;

/*
 * Contratto definito dalla business logic
 */
public interface IOutDev {
	public void display(String msg); // For HMI

	public void displayCell(ICell cell, IGrid grid); 

	public void close();

	void displayGrid(IGrid grid);
 

}