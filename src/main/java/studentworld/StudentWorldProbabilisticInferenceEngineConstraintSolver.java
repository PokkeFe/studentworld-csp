package studentworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;

import studentworld.grid.StudentWorldBoard;

public class StudentWorldProbabilisticInferenceEngineConstraintSolver {
	
	private Model model;
	private BoolVar[][] smellyCells;
	private BoolVar[][] studentCells;
	private Solver solver;

	
	public StudentWorldProbabilisticInferenceEngineConstraintSolver() {
		model = new Model("studentWorld");
		smellyCells = new BoolVar[5][5];
		studentCells = new BoolVar[5][5];
		solver = model.getSolver();
		
		initializeVariables();
		createConstraints();
	}
	
	public double generateStateProbabilitySum(List<String> allFrontierCells, List<String> smellyCells) {
		double probabilitySum = 0.0;

		updateConstraints(allFrontierCells);
		updateConstraints(smellyCells);
		setIrrelevantConstraints(allFrontierCells);
		List<String> riskyFrontierCells = removeFixedStudentCells(allFrontierCells);

		//for each possible arrangement of student presence in risky cells
		while(solver.solve()) {
			probabilitySum += generateStateProbability(riskyFrontierCells);
		}
		return probabilitySum;
	}

	private double generateStateProbability(List<String> riskyFrontierCells) {

		double stateProbability = 1.0;
		int row, col;

		for(String riskyCell : riskyFrontierCells) {
			row = getCellRow(riskyCell);
			col = getCellCol(riskyCell);
			if(studentCells[row][col].toString().endsWith("1")) {
				stateProbability *= StudentWorldBoard.STUDENT_PROBABILITY;
			} else {
				stateProbability *= (1.0 - StudentWorldBoard.STUDENT_PROBABILITY);
			}
		}
		return stateProbability;
	}
	
	private void updateConstraints(List<String> cellList) {
		int row;
		int col;
		
		for(String cellInfo : cellList) {
			row = getCellRow(cellInfo);
			col = getCellCol(cellInfo);
			
			if(cellInfo.startsWith("smelly") && cellInfo.endsWith("1")) {
				model.arithm(smellyCells[row][col], "=", 1).post();
			} else if(cellInfo.startsWith("student") || cellInfo.startsWith("fixed-student")) {
				if(cellInfo.endsWith("1")) {
					model.arithm(studentCells[row][col], "=", 1).post();
				} else if(cellInfo.endsWith("0")) {
					model.arithm(studentCells[row][col], "=", 0).post();
				}
			}
		}
	}
	
	private void setIrrelevantConstraints(List<String> riskyCellList) {
		int[] rows = new int[riskyCellList.size()];
		int[] cols = new int[riskyCellList.size()];
		boolean isPresent;
		
		for(int i=0; i<riskyCellList.size(); i++) {
			rows[i] = getCellRow(riskyCellList.get(i));
			cols[i] = getCellCol(riskyCellList.get(i));
		}
		
		
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				isPresent = false;
				//set irrelevant student cells to 0
				for(int i=0; i<riskyCellList.size(); i++) {
					if(row==rows[i] && col==cols[i]) {
						isPresent = true;
						break;
					}
				}
				if(!isPresent) {
					model.arithm(studentCells[row][col], "=", 0).post();
				}
			}
		}
	}

	private void initializeVariables() {
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				smellyCells[row][col] = model.boolVar("smelly ("+row+","+col+")");
				studentCells[row][col] = model.boolVar("student ("+row+","+col+")");
			}
		}
		model.arithm(smellyCells[0][0], "=", 0).post();
		model.arithm(studentCells[0][0], "=", 0).post();
	}
	
	private void createConstraints() {
		createStudentConstraints();
		createSmellyConstraints();
	}
	
	private void createStudentConstraints() {
		//if a cell contains a student, neighbors are smelly
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				if(row > 0) {
					model.ifThen(studentCells[row][col], model.arithm(smellyCells[row-1][col], "=", 1));
				}
				if(col < 4) {
					model.ifThen(studentCells[row][col], model.arithm(smellyCells[row][col+1], "=", 1));
				}
				if(row < 4) {
					model.ifThen(studentCells[row][col], model.arithm(smellyCells[row+1][col], "=", 1));
				}
				if(col > 0) {
					model.ifThen(studentCells[row][col], model.arithm(smellyCells[row][col-1], "=", 1));
				}
			}
		}
	}
	
	private void createSmellyConstraints() {
		ArrayList<BoolVar> neighborCellVars;
		//a smelly cell has at least one neighbor with a student
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				neighborCellVars = new ArrayList<BoolVar>();
				if(row > 0) {
					neighborCellVars.add(studentCells[row-1][col]);
				}
				if(col < 4) {
					neighborCellVars.add(studentCells[row][col+1]);
				}
				if(row < 4) {
					neighborCellVars.add(studentCells[row+1][col]);
				}
				if(col > 0) {
					neighborCellVars.add(studentCells[row][col-1]);
				}
				BoolVar[] neighborCellArray = new BoolVar[neighborCellVars.size()];
				neighborCellVars.toArray(neighborCellArray);
				model.ifThen(smellyCells[row][col], model.sum(neighborCellArray, ">=", 1));
			}
		}
	}
	
	private List<String> removeFixedStudentCells(List<String> allFrontierCells) {
		List<String> riskyFrontierCells = new ArrayList<>();
		for(String frontierCell: allFrontierCells) {
			if(!frontierCell.startsWith("fixed-student")) {
				riskyFrontierCells.add(frontierCell);
			}
		}
		return riskyFrontierCells;
	}
	
	private int getCellRow(String solverCell) {
		if(solverCell.startsWith("student")) {
			return Character.getNumericValue(solverCell.charAt(9));
		} else if(solverCell.startsWith("smelly")) {
			return Character.getNumericValue(solverCell.charAt(8));
		} else if(solverCell.startsWith("fixed-student")) {
			return Character.getNumericValue(solverCell.charAt(15));
		} else {
			return -1;
		}
	}
	
	private int getCellCol(String solverCell) {
		if(solverCell.startsWith("student")) {
			return Character.getNumericValue(solverCell.charAt(11));
		} else if(solverCell.startsWith("smelly")) {
			return Character.getNumericValue(solverCell.charAt(10));
		} else if(solverCell.startsWith("fixed-student")) {
			return Character.getNumericValue(solverCell.charAt(17));
		} else {
			return -1;
		}
	}
}