package studentworld;

import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import gridgames.data.action.Action;
import gridgames.data.action.MoveAction;
import gridgames.grid.Cell;
import studentworld.grid.StudentWorldCell;
import studentworld.player.StudentWorldPlayer;

public class StudentWorldConstraintPlanner {
	private StudentWorldPlayer player;
	private Model model;
	private int targetRow;
	private int targetCol;
	private List<IntVar> moves;
	private List<IntVar> playerCells;
	private Solver solver;
	
	public StudentWorldConstraintPlanner(StudentWorldPlayer player, int targetRow, int targetCol) {
		this.player = player;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
	}
	
	public void initializeConstraintPlanner() {
		model = new Model("studentworld planner");
		moves = new ArrayList<IntVar>();
		playerCells = new ArrayList<IntVar>();
		solver = model.getSolver();
	}
	
	public List<Action> getShortestPath() {
		int numMoves = getMinNumMoves(player.getCell(), targetRow, targetCol);
		
		do {
			initializeConstraintPlanner();
			createVariables(numMoves);
			createConstraints(numMoves);
			numMoves++;
		} while(!solver.solve());
		
		return getMoveActions();
	}
	
	private List<Action> getMoveActions() {
		List<Action> moveActions = new ArrayList<Action>();
		int plannerMove;
		for(IntVar move: moves) {
			plannerMove = move.getValue();
			if(plannerMove == 0) {
				moveActions.add(MoveAction.UP);
			} else if(plannerMove == 1) {
				moveActions.add(MoveAction.RIGHT);
			} else if(plannerMove == 2) {
				moveActions.add(MoveAction.DOWN);
			} else if(plannerMove == 3) {
				moveActions.add(MoveAction.LEFT);
			}
		}
		return moveActions;
	}
	
	private void createVariables(int numMoves) {
		for(int i=0; i<numMoves; i++) {
			playerCells.add(model.intVar("playerCell", 0, 24));
			moves.add(model.intVar("move", 0, 3));
		}
		//player cell requires one more entry to account for the player's initial location
		playerCells.add(model.intVar("playerCell", 0, 24));
	}
	
	private void createConstraints(int numMoves) {
		createPlayerCellConstraints(numMoves);
		createMoveConstraints(numMoves);
		createLocationConstraints(numMoves);
		createVisitedCellConstraints(numMoves);
	}
	
	private void createPlayerCellConstraints(int numMoves) {
		Cell cell = player.getCell();
		int playerRow = cell.getRow();
		int playerCol = cell.getCol();
		
		model.arithm(playerCells.get(0), "=", getCellNum(playerRow, playerCol)).post();
		model.arithm(playerCells.get(numMoves), "=", getCellNum(targetRow, targetCol)).post();
	}
	
	//if a player was in location x at time t and location y at time t+1, determine the move required
	private void createMoveConstraints(int numMoves) {
		int cellNum;
		for(int t=1; t<=numMoves; t++) {
			for(int row=0; row<5; row++) {
				for(int col=0; col<5; col++) {
					cellNum = getCellNum(row, col);
					if(row > 0) {
						model.ifThen(
								model.and(model.arithm(playerCells.get(t-1), "=", cellNum), model.arithm(playerCells.get(t), "=", cellNum-5)),
								model.arithm(moves.get(t-1), "=", 0));					
					}
					if(col < 4) {
						model.ifThen(
								model.and(model.arithm(playerCells.get(t-1), "=", cellNum), model.arithm(playerCells.get(t), "=", cellNum+1)),
								model.arithm(moves.get(t-1), "=", 1));
					}
					if(row < 4) {
						model.ifThen(
								model.and( model.arithm(playerCells.get(t-1), "=", cellNum), model.arithm(playerCells.get(t), "=", cellNum+5)),
								model.arithm(moves.get(t-1), "=", 2));
					}
					if(col > 0) {
						model.ifThen(
								model.and( model.arithm(playerCells.get(t-1), "=", cellNum), model.arithm(playerCells.get(t), "=", cellNum-1)),
								model.arithm(moves.get(t-1), "=", 3));
					}
				}
			}
		}
	}
	
	//if a player was in location x at time t, that player can only be 1 cell away at time t+1
	private void createLocationConstraints(int numMoves) {
		List<Constraint> constraintList;
		Constraint[] constraints;
		int cellNum;
		for(int t=1; t<=numMoves; t++) {
			for(int row=0; row<5; row++) {
				for(int col=0; col<5; col++) {
					constraintList = new ArrayList<Constraint>();
					cellNum = getCellNum(row, col);
					if(row > 0) {
						constraintList.add(model.arithm(playerCells.get(t), "=", cellNum-5));
					}
					if(col < 4) {
						constraintList.add(model.arithm(playerCells.get(t), "=", cellNum+1));
					}
					if(row < 4) {
						constraintList.add(model.arithm(playerCells.get(t), "=", cellNum+5));
					}
					if(col > 0) {
						constraintList.add(model.arithm(playerCells.get(t), "=", cellNum-1));
					}
					constraints = new Constraint[constraintList.size()];
					constraintList.toArray(constraints);
					model.ifThen(
							model.arithm(playerCells.get(t-1), "=", cellNum),
							model.or(constraints));
				}
			}
		}
	}
	
	//a player cannot be in an unvisited cell
	private void createVisitedCellConstraints(int numMoves) {
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				if(!hasVisitedCell(row, col)) {
					for(int t=0; t<numMoves; t++) {
						model.arithm(playerCells.get(t), "!=", getCellNum(row, col)).post();
					}
				}
			}
		}
	}
	
	private boolean hasVisitedCell(int row, int col) {
		for(StudentWorldCell visitedCell : player.getVisitedCells()) {
			if(row == visitedCell.getRow() && col == visitedCell.getCol()) {
				return true;
			}
		}
		return false;
	}
	
	private int getMinNumMoves(Cell startCell, int targetRow, int targetCol) {
		int startRow = startCell.getRow();
		int startCol = startCell.getCol();
		return Math.abs(targetRow-startRow) + Math.abs(targetCol-startCol);
	}
	
	private int getCellNum(int row, int col) {
		return row*5+col;
	}
}
