package dk.dtu.sb.data;

public class ReactionEvent {

	public double time;
	public Reaction reaction;
	
	public ReactionEvent(double time, Reaction r){
		this.time = time;		
		this.reaction = r;
	}
	
	public String toString(){
		return "[" + time + "] : " + reaction;
	}
}
