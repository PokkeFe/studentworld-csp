package studentworld.player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gridgames.data.action.Action;
import gridgames.display.Display;
import gridgames.grid.Cell;
import gridgames.player.Player;
import studentworld.grid.StudentWorldCell;

public class StudentWorldPlayer extends Player {
	
	private Set<StudentWorldCell> visitedCells;

	public StudentWorldPlayer(List<Action> actions, Display display, Cell initialCell) {
        super(actions, display, initialCell);
        this.visitedCells = new HashSet<StudentWorldCell>();
    }
    
    public Set<StudentWorldCell> getVisitedCells() {
    	return this.visitedCells;
    }
    
    public void addVisitedCell(StudentWorldCell cell) {
    	this.visitedCells.add(cell);
    }
	
	@Override
	public Action getAction() {
		return null;
	}
}
