package dk.dtu.AIG;

import java.util.HashSet;
import java.util.Set;

abstract class Gate {
	protected Set<Gate> in;
	protected Gate out;
	
	protected Gate(){
		in = new HashSet<Gate>();
		out = null;
	}
	
	protected void addChild(Gate g){
		//Sanity check
		assert(g != null && g.out == null);
		in.add(g);
		g.out = this;
	}
	
	/**Recursively prints all nodes in lisp notation from this root */
	public String subTreeToString(){
		String s = this.toString();
		//Except the last InputGates
		if(in!=null){
			s+="(";
			for(Gate g : in){
				s+=g.subTreeToString();
			}
		}
		return s+")";
	}
	
	// Enforce overriding of toString()
	public abstract String toString();
}

class AndGate extends Gate {
	public AndGate(){
		super();
	}
	public String toString(){
		return "And";
	}
}

class OrGate extends Gate {
	public OrGate(){
		super();
	}
	public String toString(){
		return "Or";
	}
}

class NotGate extends Gate {
	public NotGate(){
		super();
	}
	public String toString(){
		return "Not";
	}
}

class InputGate extends Gate {
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

class OutputGate extends Gate {
	String protein;
	
	public OutputGate(String protein){
		super();
		this.protein = protein;
	}
	
	public String toString(){
		return protein+" = ";
	}
}