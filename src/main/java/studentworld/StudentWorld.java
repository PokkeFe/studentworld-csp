package studentworld;

import gridgames.data.action.Action;
import gridgames.display.Display;
import gridgames.game.Game;
import gridgames.grid.Cell;
import gridgames.player.EV3Player;
import gridgames.player.HumanPlayer;
import gridgames.player.Player;
import lejos.hardware.Button;
import studentworld.data.StudentWorldPercept;
import studentworld.grid.StudentWorldBoard;
import studentworld.grid.StudentWorldCell;
import studentworld.player.CSPPlayer;
import studentworld.player.StudentWorldEV3Player;
import studentworld.player.StudentWorldPlayer;

public class StudentWorld extends Game {

    private Display display;
    private int numRows;
    private int numCols;

    public StudentWorld(Display display, int numRows, int numCols) {
    	this.numRows = numRows;
    	this.numCols = numCols;
    	this.display = display;
    	initializeBoard();
    }

    public Cell getInitialCell() {
        return this.board.getPlayerCell();
    }

    public void play(Player player) {
    	StudentWorldPlayer studentWorldPlayer = (StudentWorldPlayer) player.getGamePlayer();
    	boolean isHumanPlayer = player instanceof HumanPlayer || player instanceof CSPPlayer;
        boolean isEV3Player = player instanceof EV3Player;
        boolean isGameOver;
        Cell currentCell;
        Action move;
        boolean turnWithoutMove;
        
        do {
            currentCell = this.board.getPlayerCell();
            player.setCell(currentCell);
            for(StudentWorldPercept p : ((StudentWorldCell) player.getCell()).getPercepts()) {
        		display.addMessage(p.getMessage());
        	}
            
            if(isHumanPlayer) {
            	display.printState(false);
            } else {
            	display.printBoard(false);
            }
            
            move = player.getAction();
        	studentWorldPlayer.incrementNumActionsExecuted();
        	board.movePlayer(move);
        	
        	if(!isHumanPlayer) {
        		display.printMessages();
        	}
        	
        	isGameOver = isGameOver();
        	//move robot
        	if(player instanceof StudentWorldEV3Player) {
        		turnWithoutMove = currentCell.equals(this.board.getPlayerCell());
        		((StudentWorldEV3Player)player).processAction(move, turnWithoutMove);
        	}
        } while (!isGameOver);
        
        if (((StudentWorldBoard)board).didWin()) {
            display.addMessage("Congratulations you're free!");
        } else {
            display.addMessage("You were trapped by a student.");
        }
        display.addMessage("You made " + studentWorldPlayer.getNumActionsExecuted() + " moves.");
        display.printState(true);
        
        if(isEV3Player) {
        	Button.waitForAnyPress();
        }
    }

    private boolean isGameOver() {
        return ((StudentWorldBoard)board).didLose() || ((StudentWorldBoard)board).didWin();
    }

	@Override
	public void initializeBoard() {
		this.board = new StudentWorldBoard(numRows, numCols);
		((StudentWorldBoard)this.board).initializeBoard();
		this.display.setBoard(board);		
	}
}
