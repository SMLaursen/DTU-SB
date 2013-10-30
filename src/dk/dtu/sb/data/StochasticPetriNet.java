package dk.dtu.sb.data;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StochasticPetriNet {
	private Map<String, Reaction> reactions;
	private Map<String, Integer> initialMarkings;

	public StochasticPetriNet(){
		reactions = new HashMap<String, Reaction>();
		initialMarkings = new HashMap<String,Integer>();
	}
	
	/** Adds a reaction to the SPN.*/
	public void addReaction(Reaction r){
		if(reactions.containsKey(r.getName())){
			throw new RuntimeException("Reaction "+r+" already defined.");
		}
		reactions.put(r.getName(), r);
	}
	
	/** Removes and returns the reaction with the given name*/
	public Reaction removeReaction(String r){
		return reactions.remove(r);
	}
	
	public Reaction getReaction(String r){
		return reactions.get(r);
	}
	
	public Map<String,Reaction> getReactions(){
		return reactions;
	}
	
	/**Sets the initial markings. Replaces old values*/
	public void setInitialMarkings(String reactant, Integer value){
		initialMarkings.put(reactant, value);
	}
	
	public int getInitialMarkings(String reactant){
		return initialMarkings.get(reactant);
	}
	
	public Map<String,Integer> getInitialMarkings(){
		return initialMarkings;
	}
	
	/** Returns a file with the visual dot representation*/
	public String toGraphviz(){
		//Use http://sandbox.kidstrythisathome.com/erdos/ 
		HashSet<String> transitions = new HashSet<String>();
		String s = "digraph G {\n";
		for(Reaction r : reactions.values()){
			//Process the reactants
			for(String k : r.getReactants().keySet()){
				//Add transitions only once
				if(!transitions.contains(r.getName())){
					s+= "\""+r.getName()+" ["+r.getRate()+"]\""+" [shape=box];\n";
					transitions.add(r.getName());
				}
				s+= k+" -> "+ "\""+r.getName()+" ["+r.getRate()+"]\"";
				//Set multiplicity on edges
				if(r.getReactants().get(k) > 1){
					s+=" [label = "+r.getReactants().get(k)+"]";
				}
				s+= ";\n";
			}
			//Process the products
			for(String k : r.getProducts().keySet()){
				s+= "\""+r.getName()+" ["+r.getRate()+"]\""+" -> "+k;
				//Set multiplicity on edges
				if(r.getProducts().get(k) > 1){
					s+=" [label = "+r.getProducts().get(k)+"]";
				}
				s+= ";\n";
			}
		}
		s+= "}";
		return s;
	}
	
	/** Returns a file with the PNML encoding*/
	public File toPNML(){
		//TODO
		return null;
	}
	
	/**Prints a formatted representation of the SPN*/
	public String toString(){
		String s = "Reactions :\n";
		for(Reaction r : reactions.values()){
			s+=r.toString()+"\n";
		}
		s+="\n initial markings :";
		s+= initialMarkings;
		return s;
	}
}
