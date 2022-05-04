package studentworld.player;

import gridgames.data.action.Action;
import gridgames.ev3.EV3;
import gridgames.player.EV3Player;
import gridgames.player.Player;

public class StudentWorldEV3Player extends EV3Player {
	public StudentWorldEV3Player(EV3 ev3, Player gamePlayer) {
		super(ev3, gamePlayer);
	}
	
	public void processAction(Action move, boolean turnOnly) {
		if(turnOnly) {
			getEv3().turn(move);
		} else {
			getEv3().move(move);
		}
	}
}
