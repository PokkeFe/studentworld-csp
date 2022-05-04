package studentworld;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;

import studentworld.data.StudentWorldItem;
import studentworld.data.StudentWorldPercept;
import studentworld.grid.StudentWorldCell;
import studentworld.player.StudentWorldPlayer;

public class StudentWorldConstraintSolver {
	private Model model;
	private BoolVar[][] smellyCells;
	private BoolVar[][] glowyCells;
	private BoolVar[][] studentCells;
	private BoolVar[][] doorCells;
	private Solver solver;
	private StudentWorldPlayer player;
	
	public StudentWorldConstraintSolver(StudentWorldPlayer player) {
		model = new Model("studentWorld");
		smellyCells = new BoolVar[5][5];
		glowyCells = new BoolVar[5][5];
		studentCells = new BoolVar[5][5];
		doorCells = new BoolVar[5][5];
		solver = model.getSolver();
		this.player = player;
		
		initializeVariables();
		createConstraints();
	}
	
	private void initializeVariables() {
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				smellyCells[row][col] = model.boolVar("smelly ("+row+","+col+")");
				glowyCells[row][col] = model.boolVar("glowy ("+row+","+col+")");
				studentCells[row][col] = model.boolVar("student ("+row+","+col+")");
				doorCells[row][col] = model.boolVar("door ("+row+","+col+")");
			}
		}
	}
	
	public String getBoardState() {
		updateCurrentCellVariables();
		try {
			solver.propagate();
		} catch (ContradictionException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				sb.append(studentCells[row][col] + "\n");
				sb.append(doorCells[row][col] + "\n");
				sb.append(smellyCells[row][col] + "\n");
			}
		}
		return sb.toString();
	}
		
	private void updateCurrentCellVariables() {
		StudentWorldCell currentCell = (StudentWorldCell) player.getCell();
		int row = currentCell.getRow();
		int col = currentCell.getCol();
		
		if(currentCell.getPercepts().contains(StudentWorldPercept.SMELL)) {
			model.arithm(smellyCells[row][col], "=", 1).post();
		} else {
			model.arithm(smellyCells[row][col], "=", 0).post();
		}
		if(currentCell.getPercepts().contains(StudentWorldPercept.GLOW)) {
			model.arithm(glowyCells[row][col], "=", 1).post();
		} else {
			model.arithm(glowyCells[row][col], "=", 0).post();
		}
		if(currentCell.contains(StudentWorldItem.STUDENT)) {
			model.arithm(studentCells[row][col], "=", 1).post();
		} else {
			model.arithm(studentCells[row][col], "=", 0).post();
		}
		if(currentCell.contains(StudentWorldItem.DOOR)) {
			model.arithm(doorCells[row][col], "=", 1).post();
		} else {
			model.arithm(doorCells[row][col], "=", 0).post();
		}
	}
	
	private void createConstraints() {
		createStudentConstraints();
		createDoorConstraints();
		createSmellyConstraints();
		createGlowyConstraints();
		createOneItemPerCellConstraints();
		createFiveStudentsConstraint();
		createOneDoorConstraint();
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
	
	private void createDoorConstraints() {
		//if a cell contains a door, neighbors are glowy
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				if(row > 0) {
					model.ifThen(doorCells[row][col], model.arithm(glowyCells[row-1][col], "=", 1));
				}
				if(col < 4) {
					model.ifThen(doorCells[row][col], model.arithm(glowyCells[row][col+1], "=", 1));
				}
				if(row < 4) {
					model.ifThen(doorCells[row][col], model.arithm(glowyCells[row+1][col], "=", 1));
				}
				if(col > 0) {
					model.ifThen(doorCells[row][col], model.arithm(glowyCells[row][col-1], "=", 1));
				}
			}
		}
	}
	
	private void createGlowyConstraints() {
		ArrayList<BoolVar> neighborCellVars;
		//a glowy cell has one neighbor with the door
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				neighborCellVars = new ArrayList<BoolVar>();
				if(row > 0) {
					neighborCellVars.add(doorCells[row-1][col]);
				}
				if(col < 4) {
					neighborCellVars.add(doorCells[row][col+1]);
				}
				if(row < 4) {
					neighborCellVars.add(doorCells[row+1][col]);
				}
				if(col > 0) {
					neighborCellVars.add(doorCells[row][col-1]);
				}
				BoolVar[] neighborCellArray = new BoolVar[neighborCellVars.size()];
				neighborCellVars.toArray(neighborCellArray);
				model.ifThen(glowyCells[row][col], model.sum(neighborCellArray, "=", 1));
			}
		}
	}
	
	private void createOneItemPerCellConstraints() {
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				model.ifThen(studentCells[row][col], model.arithm(doorCells[row][col], "=", 0));
				model.ifThen(doorCells[row][col], model.arithm(studentCells[row][col], "=", 0));
				model.ifThen(doorCells[row][col], model.arithm(glowyCells[row][col], "=", 0));
				model.ifThen(glowyCells[row][col], model.arithm(doorCells[row][col], "=", 0));
			}
		}
	}
	
	private void createFiveStudentsConstraint() {
		//there are 5 students
		ArrayList<BoolVar> allStudentCells = new ArrayList<BoolVar>();
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				allStudentCells.add(studentCells[row][col]);
			}
		}
		BoolVar[] cellSumArray = new BoolVar[allStudentCells.size()];
		allStudentCells.toArray(cellSumArray);
		model.sum(cellSumArray, "=", 5).post();
	}
	
	private void createOneDoorConstraint() {
		//there is 1 door
		ArrayList<BoolVar> allDoorCells = new ArrayList<BoolVar>();
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				allDoorCells.add(doorCells[row][col]);
			}
		}
		BoolVar[] doorSumArray = new BoolVar[allDoorCells.size()];
		allDoorCells.toArray(doorSumArray);
		model.sum(doorSumArray, "=", 1).post();
	}
	
	@SuppressWarnings("unused")
	private void printAllVariables() {
		for(int row=0; row<5; row++) {
			for(int col=0; col<5; col++) {
				System.out.println(smellyCells[row][col]);
				System.out.println(glowyCells[row][col]);
				System.out.println(studentCells[row][col]);
				System.out.println(doorCells[row][col]);
			}
		}
	}
}
