package conway.domain.devices;


import conway.domain.*;
import unibo.basicomm23.utils.CommUtils;

public class MockOutdev implements IOutDev {

	@Override
	public void display(String msg) {
		CommUtils.outblue(msg);

	}


	@Override
	public void close() {

	}

	@Override
	public void displayGrid(IGrid grid) {
		// grid.printGrid();
	}

	@Override
	public void displayCell(IGrid grid, int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
