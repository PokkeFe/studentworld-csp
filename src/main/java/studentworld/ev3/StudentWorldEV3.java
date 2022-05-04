package studentworld.ev3;

import gridgames.data.Direction;
import gridgames.ev3.EV3;

public class StudentWorldEV3 extends EV3 {
	
	public StudentWorldEV3(Direction facingDirection, String programTitle, String instructions, int linearSpeed, int angularSpeed, boolean waitForPress, int numMoveUnits) {
		super(facingDirection, programTitle, instructions, linearSpeed, angularSpeed, waitForPress, numMoveUnits);
	}
}
