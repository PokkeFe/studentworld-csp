import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import gridgames.data.Direction;
import gridgames.data.action.Action;
import gridgames.data.action.MoveAction;
import gridgames.display.ConsoleDisplay;
import gridgames.display.Display;
import gridgames.display.EV3Display;
import gridgames.ev3.EV3;
import gridgames.grid.Cell;
import gridgames.player.EV3Player;
import gridgames.player.HumanPlayer;
import gridgames.player.Player;
import studentworld.StudentWorld;
import studentworld.ev3.StudentWorldEV3;
import studentworld.player.CSPPlayer;
import studentworld.player.StudentWorldEV3Player;
import studentworld.player.StudentWorldPlayer;

public class Main {
    public static void main(String[] args) {
    	List<Action> allActions = Arrays.asList(MoveAction.MOVE_ACTIONS);
    	if(args.length > 0 && "-console".equals(args[0])) {
    		runOnConsole(allActions);
    	} else {
    		runOnRobot(allActions);
    	}
    }
    
    public static void runOnConsole(List<Action> allActions) {
    	Scanner scanner = new Scanner(System.in);
    	Display display = new ConsoleDisplay();
        String choice;
        Player player = null;
        StudentWorld game = null;
        
        do {
        	game = new StudentWorld(display, 5, 5);
        	player = getPlayer(scanner, game, display);
            do {
            	game.play(player);
                System.out.print("Play again? [YES, NO]: ");
                choice = scanner.next().toLowerCase();
            } while(!choice.equals("yes") && !choice.equals("no"));
        } while(choice.equals("yes"));
        scanner.close();
    }
    
    public static void runOnRobot(List<Action> allActions) {
    	EV3Display display = new EV3Display();
    	StudentWorld game = new StudentWorld(display, 5, 5);
        Cell initialCell = game.getInitialCell();
        String instructions = "Place the robot in the upper left most cell facing right";
        EV3 ev3 = new StudentWorldEV3(Direction.RIGHT, "StudentWorld", instructions, 100, 50, true, 100);
        Player p = new StudentWorldPlayer(MoveAction.getAllActions(), display, initialCell);
        EV3Player robot = new StudentWorldEV3Player(ev3, p);
        display.setEv3Display(ev3.getDisplay());
        ev3.displayInstructions();
        game.play(robot);
    }
    
    private static Player getPlayer(Scanner scanner, StudentWorld game, Display display) {
    	List<Action> actions = Arrays.asList(MoveAction.MOVE_ACTIONS);
    	Cell initialCell = game.getInitialCell();
    	Player player = null;
    	String choice;
    	 do {
             System.out.print("Human play or computer play? [HUMAN, COMPUTER]: ");
             choice = scanner.next().toLowerCase();
         } while(!choice.equals("human") && !choice.equals("computer"));
    	 
    	 if(choice.equals("human")) {
    		 StudentWorldPlayer studentWorldPlayer = new StudentWorldPlayer(actions, display, initialCell);
    		 player = new HumanPlayer(studentWorldPlayer, scanner);
         } else {
    		 player = new CSPPlayer(actions, display, initialCell);
         }
    	return player;
    }
}