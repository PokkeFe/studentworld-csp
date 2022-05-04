package studentworld.grid;

import java.util.Set;
import java.util.TreeSet;

import gridgames.data.item.Item;
import gridgames.grid.Cell;
import studentworld.data.StudentWorldItem;
import studentworld.data.StudentWorldPercept;

public class StudentWorldCell extends Cell {
 
    private Set<StudentWorldPercept> percepts;

    public StudentWorldCell(int row, int col) {
        super(row, col);
        this.percepts = new TreeSet<StudentWorldPercept>();
    }

    public StudentWorldCell(int row, int col, StudentWorldItem item) {
        this(row, col);
        this.add(item);
    }

    public Set<StudentWorldPercept> getPercepts() {
        return this.percepts;
    }

    public void addPercept(StudentWorldPercept p) {
        this.percepts.add(p);
    }

    public void addPerceptForItem(Item i) {
        if(StudentWorldItem.STUDENT.equals(i)) {
            addPercept(StudentWorldPercept.SMELL);
        } else if(StudentWorldItem.DOOR.equals(i)) {
            addPercept(StudentWorldPercept.GLOW);
        }
    }
}
