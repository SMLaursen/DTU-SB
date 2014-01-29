package dk.dtu.techmap;

import java.util.HashSet;
import java.util.Set;

public abstract class LogicGate {
	protected Set<LogicGate> in;
	protected LogicGate out;
	private static int idCounter = 0;
	protected int myId;
	
	protected LogicGate(){
		in = new HashSet<LogicGate>();
		out = null;
		myId = idCounter++;
	}
	
	protected void addChild(LogicGate g){
		//Sanity check
		assert(g != null);
		in.add(g);
		g.out = this;
	}
	
	/**Recursively prints all nodes in preorder-lisp notation from this Gate as root */
	public String subTreeToString(){
		String s = this.toString();
		//Except the last InputGates
		if(in!=null){
			s+="(";
			for(LogicGate g : in){
				s+=g.subTreeToString();
			}
		}
		return s+")";
	}
	
	// Enforce overriding of toString()
	public abstract String toString();
	
	/** Removes the child object */
	protected void removeChild(LogicGate g){
		in.remove(g);
		g.out = null;
	}
}

class AndGate extends LogicGate {
	public AndGate(){
		super();
	}
	public String toString(){
		return "And";
	}
}

class OrGate extends LogicGate {
	public OrGate(){
		super();
	}
	public String toString(){
		return "Or";
	}
}

class NotGate extends LogicGate {
	public NotGate(){
		super();
	}
	public String toString(){
		return "Not";
	}
}

class InputGate extends LogicGate {
	String protein;

	public InputGate(String protein){
		out = null;
		//Do net initialize in!
		this.protein = protein;
	}
	public String toString(){
		return protein;
	}
}

class OutputGate extends LogicGate {
	String protein;
	
	public OutputGate(String protein){
		super();
		this.protein = protein;
	}
	
	public String toString(){
		return protein+" = ";
	}
}