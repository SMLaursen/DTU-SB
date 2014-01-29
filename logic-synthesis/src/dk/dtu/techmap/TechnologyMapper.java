package dk.dtu.techmap;

import java.util.HashMap;
import java.util.HashSet;

public class TechnologyMapper {

	//Stores the LogicGates that needs to be matched next by the protein .
//	private HashMap<String,LogicGate> matchNext = new HashMap<String,LogicGate>();

	
	private AIG graph;

	/**Sets up the technologymapper using g to be matched */
	public TechnologyMapper(AIG g){
		//Ensure AIG has been correctly defined.
		assert(g.output instanceof OutputGate);
		assert(g.isAIG);
		//Add top-level node
		graph = g;

	}
	/** Starts mapping.
	 * @return
	 * null : No mapping could be made
	 * Set : The set of parts in the solution */
	public HashSet<AIG> start(HashSet<AIG> library){
		HashMap<String, LogicGate> startingGate = new HashMap<String, LogicGate>();
		startingGate.put(graph.output.toString(), graph.output);
		HashSet<AIG> solution = new HashSet<AIG>();
		return map(library, solution, startingGate);
		
	}
	
	/** Maps using the library of {@link AIG}'s
	 * 
	 * @return
	 * null : no match could be found
	 * Set  : the set of parts that make up the match*/
	private HashSet<AIG> map(HashSet<AIG> library, HashSet<AIG> selectedParts, HashMap<String,LogicGate> toMatch){

		//For each incomplete matching
		for(LogicGate g : toMatch.values()){

			//Try to match it using any part from library
			for(AIG libPart : library){

				//Ensure orthogonality
				if(selectedParts.contains(libPart)){
					continue;
				}
				HashMap<String, LogicGate> toMatchNext = isMatching(libPart.getOutputGate(), g);

				//Part didn't match. skip this part.
				if(toMatchNext==null){
					continue;
				}//Entire graph has been matched! 
				else if(toMatchNext.isEmpty()){
					//Record score / Output solution
					selectedParts.add(libPart);
					return selectedParts;
				}//Further matching should be conducted  
				else {
					//Add this to selected parts
					selectedParts.add(libPart);			

					//Recursively match remainder
					HashSet<AIG> s = map(library, selectedParts, toMatchNext);

					//If this branch didn't lead to a solution, remove part again and try matching using next part. 
					if(s == null){
						selectedParts.remove(libPart);
						continue;
					} else {
						return s;
					}					
				}
			}
			//When all available parts have been tried matched, it is not possible to match using this branch.
			return null;
		}
		//Should only be here if toMatch initially are empty
		assert(false);
		return null;

	}

	/** Checks whether 'other' matches the subtree at 'atPos' 
	 * @return
	 * null : incompatible library part.
	 * empty map : compatible library part that can cover the entire remaining AIG. 
	 * non-empty map : compatible library part - but further matching is necessary. 
	 * */
	public HashMap<String, LogicGate> isMatching(LogicGate other, LogicGate atPos){
		//Used as control : null = no match, empty = complete match 
		//Values = partial match i.e. need to continue explore these vales with other parts
		HashMap<String, LogicGate> matches = new HashMap<String, LogicGate>();

		if(other instanceof AndGate){
			if(atPos instanceof AndGate){
				//Map for storing intersections
				HashMap<String, LogicGate> mergedMatches = new HashMap<String, LogicGate>();
				//Sanity check
				assert(atPos.in.size() == 2);
				assert(other.in.size() == 2);

				//Counts the numbers of branches that are matching
				int match = 0;

				//TODO : Gives false positives

				//To remember if a branch already has been matched.
				LogicGate alreadyMatched = null;

				//Check children
				for(LogicGate childThis : atPos.in){
					for(LogicGate childOther : other.in){
						//If this branch has already been matched, then skip it.
						if(alreadyMatched != null && alreadyMatched == childOther){
							continue;
						}
						else if(childOther.getClass() == childThis.getClass()){
							matches = isMatching(childOther,childThis);
							//See if it matches downwards.
							if(matches != null){
								//Found one match 
								match++;
								alreadyMatched = childOther;
								mergedMatches.putAll(matches);
								//If both branches could be matched, return their union.
								if(match == 2){
									return mergedMatches;
								} 
								//Else break (And check the other branch)
								break;
							}
						}
					}
				}
				//return null when every branch has been tried matched.
				return null;
			}

		} else if(other instanceof NotGate){
			if(atPos instanceof NotGate){
				//Sanity checks
				assert(other.in.size() == 1);
				assert(atPos.in.size() == 1);
				//Recurse on both children
				LogicGate childOther = (LogicGate) other.in.toArray()[0];
				LogicGate childThis = (LogicGate) atPos.in.toArray()[0];
				return isMatching(childOther, childThis);
			}
			//Else no match
			return null;
			
		} else if(other instanceof InputGate){
			//If the input proteins matches, we have a "complete" match.
			if(!(atPos instanceof InputGate && other.toString().equals(atPos.toString()) )){
				//Otherwise store position of graph where to resume matching next part
				//If not wrong mappings! (Orthogonality ensures we can abort here)
				if(graph.inMap.containsKey(atPos.toString())){
					return null;
				} else {
					matches.put(other.toString(),atPos);
				}
			}
			return matches;
		} else if(other instanceof OutputGate){
			//Sanity check
			assert(other.in.size() == 1);
			//Proceed to next node
			LogicGate childOther = (LogicGate) other.in.toArray()[0];
			LogicGate childThis = atPos;
			if(atPos instanceof OutputGate){
				assert(atPos.in.size() == 1);
				childThis = (LogicGate) atPos.in.toArray()[0];
			}
			return isMatching(childOther, childThis);
		} else {
			throw new RuntimeException("Unrecognized graph structure");
		}
		return null;
	}
}
