package studentworld.player;

import java.util.ArrayList;
import java.util.List;

import gridgames.data.action.Action;
import gridgames.display.Display;
import gridgames.grid.Cell;
import studentworld.StudentWorldConstraintPlanner;
import studentworld.StudentWorldConstraintSolver;
import studentworld.StudentWorldProbabilisticInferenceEngine;
import studentworld.grid.StudentWorldCell;

public class CSPPlayer extends StudentWorldPlayer {
	
	private StudentWorldConstraintSolver swConstraintSolver;
	private List<Action> moves;
	
	public CSPPlayer(List<Action> actions, Display display, Cell initialCell) {
		super(actions, display, initialCell);
		swConstraintSolver = new StudentWorldConstraintSolver(this);
		moves = new ArrayList<Action>();
	}
	
	@Override
	public Action getAction() {
		addVisitedCell((StudentWorldCell) getCell());
		if(moves.isEmpty()) {
			String solverBoard = swConstraintSolver.getBoardState();
			String targetCell = getTargetCell(solverBoard);
			int row = getTargetCellRow(targetCell);
			int col = getTargetCellCol(targetCell);
			StudentWorldConstraintPlanner swConstraintPlanner = new StudentWorldConstraintPlanner(this, row, col);
			moves = swConstraintPlanner.getShortestPath();
		}
		return moves.remove(0);
	}
	
	private int getTargetCellRow(String targetCell) {
		if(targetCell.startsWith("student")) {
			return Character.getNumericValue(targetCell.charAt(9));
		} else {
			return Character.getNumericValue(targetCell.charAt(6));
		}
	}
	
	private int getTargetCellCol(String targetCell) {
		if(targetCell.startsWith("student")) {
			return Character.getNumericValue(targetCell.charAt(11));
		} else {
			return Character.getNumericValue(targetCell.charAt(8));
		}
	}
	
	private boolean visitedCellsContains(int row, int col) {
		for(StudentWorldCell visitedCell : getVisitedCells()) {
			if(visitedCell.getRow() == row && visitedCell.getCol() == col) {
				return true;
			}
		}
		return false;
	}
	
	private String getTargetCell(String solverBoard) {
		List<String> frontierCells;
		String closestSafeFrontierCell;
		String doorCell = getDoorCell(solverBoard);
		Cell currentCell = getCell();
		
		//if door cell is known, go there
		if(doorCell != null) {
			return doorCell;
		}
		
		frontierCells = getFrontierCells(solverBoard);
		closestSafeFrontierCell = getClosestSafeFrontierCell(frontierCells, currentCell);
		//otherwise, if safe frontier cell is known, go there
		if(closestSafeFrontierCell != null && !closestSafeFrontierCell.isEmpty()) {
			return closestSafeFrontierCell;
		}
		//all options are risky, so defer to the probabilistic inference engine
		List<String> smellyCells = getSmellyCells(solverBoard);
		return StudentWorldProbabilisticInferenceEngine.getTargetCell(frontierCells, smellyCells);
	}
	
	private List<String> getFrontierCells(String solverBoard) {
		int parenIndex;
		int row;
		int col;
		
		List<String> frontierCells = new ArrayList<String>();
		String[] allCellInfo = solverBoard.split("\n");
		for(String cellInfo : allCellInfo) {
			if(cellInfo.startsWith("student")) {
				parenIndex = cellInfo.indexOf("(");
				row = Character.getNumericValue(cellInfo.charAt(parenIndex+1));
				col = Character.getNumericValue(cellInfo.charAt(parenIndex+3));		
				if(isOnFrontier(row, col)) {
					frontierCells.add(cellInfo);
				}
			}
		}
		return frontierCells;
	}
	
	private List<String> getSmellyCells(String solverBoard) {		
		List<String> smellyCells = new ArrayList<String>();
		String[] allCellInfo = solverBoard.split("\n");
		for(String cellInfo : allCellInfo) {
			if(cellInfo.startsWith("smelly") && cellInfo.endsWith("1")) {	
				smellyCells.add(cellInfo);
			}
		}
		return smellyCells;
	}
	
	private boolean isOnFrontier(int row, int col) {
		int visitedRow;
		int visitedCol;
		
		//if the cell has been visited, it is not on the frontier
		if(visitedCellsContains(row, col)) {
			return false;
		}
		
		//if the cell is adjacent to a visited cell, it is on the frontier
		for(Cell visitedCell : getVisitedCells()) {
			visitedRow = visitedCell.getRow();
			visitedCol = visitedCell.getCol();
			if(Math.abs(visitedRow - row) + Math.abs(visitedCol - col) == 1) {
				return true;
			}
		}
		
		return false;
	}
	
	private String getDoorCell(String solverBoard) {
		String[] allCellInfo = solverBoard.split("\n");
		for(String cellInfo : allCellInfo) {
			if(cellInfo.startsWith("door") && cellInfo.endsWith("1")) {
				return cellInfo;
			}
		}
		return null;
	}
	
	private String getClosestSafeFrontierCell(List<String> frontierCells, Cell currentCell) {
		return getClosestFrontierCell(frontierCells, currentCell, true);
	}
	
	private String getClosestFrontierCell(List<String> frontierCells, Cell currentCell, boolean isSafe) {
		String closestSafeCell = null;
		int closestDistance = Integer.MAX_VALUE;
		int currentRow = currentCell.getRow();
		int currentCol = currentCell.getCol();
		int parenIndex;
		int row;
		int col;
		int distance;
		String cellStatus;
		
		if(isSafe) {
			cellStatus = "0";
		} else {
			cellStatus = "]";
		}
		
		for(String cellInfo : frontierCells) {
			//if sell is safe
			if(cellInfo.startsWith("student") && cellInfo.endsWith(cellStatus)) {
				parenIndex = cellInfo.indexOf("(");
				row = Character.getNumericValue(cellInfo.charAt(parenIndex+1));
				col = Character.getNumericValue(cellInfo.charAt(parenIndex+3));
				distance = Math.abs(currentRow - row) + Math.abs(currentCol - col);
				//if distance is less than closestDistance
				if(distance < closestDistance) {
					closestSafeCell = cellInfo;
					closestDistance = distance;
				}
			}
		}
		return closestSafeCell;
	}
}
