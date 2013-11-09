package dk.dtu.sb.data;

/**
 * 
 */
public class ReactionEvent {

    /**
     * 
     */
	public double time;
	
	/**
	 * 
	 */
	public Reaction reaction;
	
	/**
	 * Default constructor.
	 * 
	 * @param time
	 * @param reaction See {@link Reaction}.
	 */
	public ReactionEvent(double time, Reaction reaction){
		this.time = time;		
		this.reaction = reaction;
	}
	
	public String toString(){
		return "[" + time + "] : " + reaction;
	}
}
