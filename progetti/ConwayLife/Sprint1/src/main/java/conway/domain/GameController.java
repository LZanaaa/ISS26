package conway.domain;

public interface GameController {
	 int getGenTime();
	 void onStart();
	 void onStop();
	 void onClear();
	 int numEpoch();
	 void switchCellStatus(int x, int y);
}
