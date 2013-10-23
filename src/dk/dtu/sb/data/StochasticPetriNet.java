package dk.dtu.sb.data;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StochasticPetriNet {
	//Maps reaction and reactant names to indexes.
	private Map<String, Integer> transitions;
	private Map<String, Integer> places;
	private int transitionIndex;
	private int placeIndex;
	
	private int[][] reactionMatrix;
	
	private int[] initialMarkings;
	
	private double[] rates;
	
	/** Constructor */
	public StochasticPetriNet(int noOfReactions, int noOfReactants){
		transitions = new HashMap<String,Integer>(noOfReactions);
		transitionIndex = 0;
		
		places = new HashMap<String,Integer>(noOfReactants);
		placeIndex = 0;
		
		reactionMatrix = new int[noOfReactions][noOfReactants];
		
		rates = new double[noOfReactions];
		
		initialMarkings = new int[noOfReactants];
	}
	
	/** Sets the given reaction*/
	public void setReaction(String reaction,Map<String,Integer> reactants, double rate){
		//Map reaction to index and update rate vector
		if(!transitions.containsKey(reaction)){
			transitions.put(reaction, transitionIndex);
			rates[transitionIndex] = rate;
		}else{
			throw new RuntimeException("Duplicate reaction name");
		}
		//Map each unseen reactant to an index an update the reactionmatrix.
		for(String s : reactants.keySet()){
			if(!places.containsKey(s)){
				places.put(s, placeIndex++);
			}
			reactionMatrix[transitionIndex][places.get(s)] = reactants.get(s);
		}
		transitionIndex++;
	}
	
	/**Sets the initial markings*/
	public void setInitialMarkings(Map<String,Integer> markings){
		for(String place : markings.keySet()){
			initialMarkings[places.get(place)] = markings.get(place);
		}
	}
	
	public int[][] getReactionMatrix(){
		return reactionMatrix;
	}
	
	public int[] getInitialMarkings(){
		return initialMarkings;
	}
 	
	public double[] getRates(){
		return rates;
	}
	
	/** Returns a file with the visual dot representation*/
	public File getGraphviz(){
		//TODO
		return null;
	}
	
	/** Returns a file with the PNML encoding*/
	public File getPNML(){
		//TODO
		return null;
	}
	
	/**Prints a formatted representation of the SPN*/
	public String toString(){
		String s = "reaction matrix :\n";
		s += Arrays.deepToString(reactionMatrix).replace("], ", "]\n");
		s += "\n\n initial markings :\n\n"+initialMarkings.toString();
		s += "\n\n rates :\n\n"+rates.toString();
		s += "\n\n place mappings :\n\n"+places.toString();
		s += "\n\n transition mappings :\n\n"+transitions.toString();
		return s;
	}
}
