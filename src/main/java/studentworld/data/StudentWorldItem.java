package studentworld.data;

import gridgames.data.item.Item;

public enum StudentWorldItem implements Item {
    STUDENT,
    DOOR;

    @Override
    public String toString() {
        if(this.equals(STUDENT)) {
            return "S";
        } else if(this.equals(StudentWorldItem.DOOR)) {
            return "D";
        } else {
            return "";
        }
    }

	@Override
	public boolean isHiddenItem() {
		return true;
	}
}
