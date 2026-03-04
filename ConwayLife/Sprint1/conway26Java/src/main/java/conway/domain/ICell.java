package main.java.conway.domain;

public interface ICell {
    
    // _-- PRIMITIVE --- --> Ottenute conoscendo strutturalmete la cella e le sue proprietà
	void setStatus(boolean status); //Permette di impostare lo stato della cella (viva o morta).
	boolean isAlive(); //Proprietà della cella, una volta invocata determina lo stato della cella.

    // -- NON PRIMITIVE --- --> Ottenute senza dover conoscere la struttura della cella e le sue proprietà, ma solo invocando i metodi della cella
	void switchCellStatus(); //Permette di modificare lo stato della cella (da viva a morta o viceversa)
	
    }
