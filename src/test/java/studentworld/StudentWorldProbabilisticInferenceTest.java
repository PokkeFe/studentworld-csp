package studentworld;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StudentWorldProbabilisticInferenceTest {

	@Test
	public void testGenerateStateProbabilitySumOne() {
		try {
			List<String> smellyCells = generateSmellyCellsForCaseOne();

			StudentWorldProbabilisticInferenceEngineConstraintSolver swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			List<String> frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(0, "fixed-student (0,2) = 1");
			double probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			double expectedProbabilitySum = 0.36;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (0,2) with a student, and (1,1) and (2,0) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(0, "fixed-student (0,2) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.2;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (0,2) without a student, and (1,1) and (2,0) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(1, "fixed-student (1,1) = 1");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 1;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (1,1) with a student, and (0,2) and (2,0) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(1, "fixed-student (1,1) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.04;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (1,1) without a student, and (0,2) and (2,0) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(2, "fixed-student (2,0) = 1");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.36;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (2,0) with a student, and (0,2) and (1,1) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFrontierCellsForCaseOne();
			frontierCells.set(2, "fixed-student (2,0) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.2;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,1) and (1,0) smelly, (2,0) without a student, and (0,2) and (1,1) risky");

		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	@Test
	public void testGenerateStateProbabilitySumTwo() {
		try {
			List<String> smellyCells = generateSmellyCellsForCaseTwo();

			StudentWorldProbabilisticInferenceEngineConstraintSolver swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			List<String> frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(0, "fixed-student (0,4) = 1");
			double probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			double expectedProbabilitySum = 1;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (0,4), (2,2) and (4,1) with a student, and (1,3) and (3,2) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(0, "fixed-student (0,4) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.2;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (0,4) without a student, (2,2) and (4,1) with a student, and (1,3) and (3,2) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(1, "fixed-student (1,3) = 1");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 1;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (1,3), (2,2) and (4,1) with a student, and (0,4) and (3,2) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(1, "fixed-student (1,3) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = 0.2;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (1,3) without a student, (2,2) and (4,1) with a student, and (0,4) and (3,2) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(3, "fixed-student (3,2) = 1");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = .36;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (2,2), (3,2) and (4,1) with a student, and (0,4) and (1,3) risky");

			swpiecs = new StudentWorldProbabilisticInferenceEngineConstraintSolver();
			frontierCells = generateFixedFrontierCellsForCaseTwo();
			frontierCells.set(3, "fixed-student (3,2) = 0");
			probabilitySum = swpiecs.generateStateProbabilitySum(frontierCells, smellyCells);
			probabilitySum = Math.floor(probabilitySum*100)/100.0;
			expectedProbabilitySum = .36;
			assertEquals(expectedProbabilitySum, probabilitySum, "generateStateProbabilitySum not correctly computing the sum of all consistent frontiers with (0,3), (1,2), (2,1), (3,1), and (4,0) smelly, (3,2) without a student, (2,2) and (4,1) with a student, and (0,4) and (1,3) risky");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	@Test
	public void testGetTargetCellOne() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseOne();
			List<String> smellyCells = generateSmellyCellsForCaseOne();
			String targetCell = StudentWorldProbabilisticInferenceEngine.getTargetCell(riskyFringeCells, smellyCells);
			String[] expectedTargetCells = {"student (0,2) = [0,1]", "student (2,0) = [0,1]"};
			if(!Arrays.asList(expectedTargetCells).contains(targetCell)) {
				fail("getTargetCell returning incorrect cell: " + targetCell);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testCalculateStudentProbabilityForCellOne() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseOne();
			List<String> smellyCells = generateSmellyCellsForCaseOne();
			
			Double p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (0,2) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			Double expectedP = 0.31;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (0,2) is fixed in case one");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (1,1) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.86;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (1,1) is fixed in case one");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (2,0) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.31;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (2,0) is fixed in case one");

			
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testGetTargetCellTwo() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseTwo();
			List<String> smellyCells = generateSmellyCellsForCaseTwo();
			String targetCell = StudentWorldProbabilisticInferenceEngine.getTargetCell(riskyFringeCells, smellyCells);
			String expectedTargetCell = "student (3,2) = [0,1]";
			assertEquals(expectedTargetCell, targetCell, "getTargetCell not returning correct cell");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testCalculateStudentProbabilityForCellTwo() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseTwo();
			List<String> smellyCells = generateSmellyCellsForCaseTwo();
			
			Double p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (0,4) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			Double expectedP = 0.55;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (0,4) is fixed in case two");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (1,3) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.55;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (1,3) is fixed in case two");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (3,2) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.20;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (3,2) is fixed in case two");

			
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testGetTargetCellThree() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseThree();
			List<String> smellyCells = generateSmellyCellsForCaseThree();
			String targetCell = StudentWorldProbabilisticInferenceEngine.getTargetCell(riskyFringeCells, smellyCells);
			String[] expectedTargetCells = {"student (3,1) = [0,1]", "student (4,2) = [0,1]"};
			if(!Arrays.asList(expectedTargetCells).contains(targetCell)) {
				fail("getTargetCell returning incorrect cell: " + targetCell);
			}		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testCalculateStudentProbabilityForCellThree() {
		try {
			List<String> riskyFringeCells = generateFrontierCellsForCaseThree();
			List<String> smellyCells = generateSmellyCellsForCaseThree();
			
			Double p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (2,4) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			Double expectedP = 0.37;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (2,4) is fixed in case three");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (3,1) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.27;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (3,1) is fixed in case three");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (3,3) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.77;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (3,3) is fixed in case three");
			
			p = StudentWorldProbabilisticInferenceEngine.calculateStudentProbabilityForCell("student (4,2) = [0,1]", riskyFringeCells, smellyCells);
			p = Math.floor(p*100)/100.0;
			expectedP = 0.27;
			assertEquals(expectedP, p, "calculateStudentProbabilityForCell incorrect when (4,2) is fixed in case three");

			
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	private List<String> generateFrontierCellsForCaseOne() {
		List<String> riskyFringeCells = new ArrayList<String>();
		riskyFringeCells.add("student (0,2) = [0,1]");
		riskyFringeCells.add("student (1,1) = [0,1]");
		riskyFringeCells.add("student (2,0) = [0,1]");
		return riskyFringeCells;
	}
	
	private List<String> generateSmellyCellsForCaseOne() {
		List<String> smellyCells = new ArrayList<String>();
		smellyCells.add("smelly (0,1) = 1");
		smellyCells.add("smelly (1,0) = 1");
		return smellyCells;
	}
	
	private List<String> generateFrontierCellsForCaseTwo() {
		List<String> riskyFringeCells = new ArrayList<String>();
		riskyFringeCells.add("student (0,4) = [0,1]");
		riskyFringeCells.add("student (1,3) = [0,1]");
		riskyFringeCells.add("student (2,2) = 1");
		riskyFringeCells.add("student (3,2) = [0,1]");
		riskyFringeCells.add("student (4,1) = 1");
		return riskyFringeCells;
	}

	private List<String> generateFixedFrontierCellsForCaseTwo() {
		List<String> riskyFringeCells = new ArrayList<String>();
		riskyFringeCells.add("student (0,4) = [0,1]");
		riskyFringeCells.add("student (1,3) = [0,1]");
		riskyFringeCells.add("fixed-student (2,2) = 1");
		riskyFringeCells.add("student (3,2) = [0,1]");
		riskyFringeCells.add("fixed-student (4,1) = 1");
		return riskyFringeCells;
	}
	
	private List<String> generateSmellyCellsForCaseTwo() {
		List<String> smellyCells = new ArrayList<String>();
		smellyCells.add("smelly (0,3) = 1");
		smellyCells.add("smelly (1,2) = 1");
		smellyCells.add("smelly (2,1) = 1");
		smellyCells.add("smelly (3,1) = 1");
		smellyCells.add("smelly (4,0) = 1");
		return smellyCells;
	}
	
	private List<String> generateFrontierCellsForCaseThree() {
		List<String> riskyFringeCells = new ArrayList<String>();
		riskyFringeCells.add("student (0,4) = 1");
		riskyFringeCells.add("student (2,0) = 1");
		riskyFringeCells.add("student (2,4) = [0,1]");
		riskyFringeCells.add("student (3,1) = [0,1]");
		riskyFringeCells.add("student (3,3) = [0,1]");
		riskyFringeCells.add("student (4,2) = [0,1]");
		return riskyFringeCells;
	}
	
	private List<String> generateSmellyCellsForCaseThree() {
		List<String> smellyCells = new ArrayList<String>();
		smellyCells.add("smelly (0,3) = 1");
		smellyCells.add("smelly (1,0) = 1");
		smellyCells.add("smelly (1,4) = 1");
		smellyCells.add("smelly (2,1) = 1");
		smellyCells.add("smelly (2,3) = 1");
		smellyCells.add("smelly (3,2) = 1");
		return smellyCells;
	}
}
