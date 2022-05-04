package studentworld.data;

public enum StudentWorldPercept {
    SMELL,
    GLOW;
	
	public String getMessage() {
		if(this.equals(SMELL)) {
			return "You smell something... funky"; 
		} else if(this.equals(GLOW)) {
			return "You see the warm glow of the exit sign";
		} else {
			return "";
		}
	}
}
