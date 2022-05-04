package studentworld;

import java.util.ArrayList;
import java.util.List;


import studentworld.grid.StudentWorldBoard;

public class StudentWorldProbabilisticInferenceEngine {
	
	public static String getTargetCell(List<String> allFrontierCells, List<String> smellyCells) {
		// Get the risky cells from all frontier cells
		List<String> riskyCells = generateRiskyFrontierCells(allFrontierCells);
		// Declare and initialize the starting values for tracking least risky cell
		double leastRiskyCellProbability = 1.0;
		String leastRiskyCell = riskyCells.get(0);
		// Loop over all risky cells
		for(String riskyCell : riskyCells) {
			// Get the probability of a student existing in that cell
			double thisStudentProbability = calculateStudentProbabilityForCell(riskyCell, allFrontierCells, smellyCells);
			// If this cell is less risky than the least risky cell, set as the least risky cell
			if(thisStudentProbability < leastRiskyCellProbability) {
				leastRiskyCellProbability = thisStudentProbability;
				leastRiskyCell = riskyCell;
			}
		}
		// Return the least risky cell
		return leastRiskyCell;
	}
	
	public static double calculateStudentProbabilityForCell(String riskyFrontierCellToFix, List<String> allFrontierCells, List<String> smellyCells) {
		// Set the probability of a student existing in this cell to the student probability * the sum of all probabilities of consistent frontiers
		double studentProbabilitySum = (StudentWorldBoard.STUDENT_PROBABILITY) * calculateProbabilitySum(riskyFrontierCellToFix, true, allFrontierCells, smellyCells);
		// Set the probability of a student not existing in this cell to the no-student probability * the sum of all probabilities of consistent frontiers
		double noStudentProbabilitySum = (1.0 - StudentWorldBoard.STUDENT_PROBABILITY) * calculateProbabilitySum(riskyFrontierCellToFix, false, allFrontierCells, smellyCells);
		// Generate the normalized alpha
		double normalizedAlpha = 1.0 / (studentProbabilitySum + noStudentProbabilitySum);
		// Multiply the student probability by the normalized alpha (We can ignore the no student probability sum, as we're not using both terms of the vector)
		studentProbabilitySum *= normalizedAlpha;
		// Return the normalized value
		return studentProbabilitySum;
	}
	
	public static double calculateProbabilitySum(String riskyFrontierCellToFix, boolean riskyFrontierCellHasStudent, List<String> allFrontierCells, List<String> smellyCells) {
		StudentWorldProbabilisticInferenceEngineConstraintSolver swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
		allFrontierCells = new ArrayList<>(allFrontierCells);
		String riskyCell;

		//update model to assume a student is or is not present in the given cell
		for(int i=0; i<allFrontierCells.size(); i++) {
			riskyCell = allFrontierCells.get(i);
			if(riskyCell.equals(riskyFrontierCellToFix)) {
				if(riskyFrontierCellHasStudent) {
					riskyCell = riskyCell.replace("= [0,1]", "= 1").replace("student", "fixed-student");
				} else {
					riskyCell = riskyCell.replace("= [0,1]", "= 0").replace("student", "fixed-student");
				}
				allFrontierCells.set(i, riskyCell);
				break;
			}
		}
		//return the probability sum given student presence or no student presence in the given cell
		return swpiecs.generateStateProbabilitySum(allFrontierCells, smellyCells);
	}
	
	private static List<String> generateRiskyFrontierCells(List<String> allFrontierCells) {
		List<String> riskyFrontierCells = new ArrayList<>();
		String riskyCell;
		for(int i=0; i<allFrontierCells.size(); i++) {
			riskyCell = allFrontierCells.get(i);
			if(riskyCell.startsWith("student") && riskyCell.endsWith("1")) {
				riskyCell = riskyCell.replace("student", "fixed-student");
				allFrontierCells.set(i, riskyCell);
			} else {
				riskyFrontierCells.add(riskyCell);
			}
		}
		return riskyFrontierCells;
	}
}