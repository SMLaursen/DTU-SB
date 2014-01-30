package dk.dtu.techmap;

import java.util.HashMap;
import java.util.HashSet;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;

public class TechnologyMapper {
	private AIG graph;

	/**Sets up the technologymapper using g to be matched */
	public TechnologyMapper(AIG g){
		//Ensure AIG has been correctly defined.
		assert(g.getOutputGate() instanceof OutputGate);
		assert(g.isAIG);
		//Add top-level node
		graph = g;
	}
	
	/** Starts mapping.
	 * @return
	 * null : No mapping could be made
	 * Set : The set of parts in the solution */
	public HashSet<AIG> start(){
		HashMap<String, LogicGate> startingGate = new HashMap<String, LogicGate>();
		startingGate.put(graph.getOutputGate().getProtein(), graph.getOutputGate());
		HashSet<AIG> solution = new HashSet<AIG>();
		return map(solution, startingGate);
	}
	
	/** Maps using the library of {@link AIG}'s
	 * 
	 * @return
	 * null : no match could be found
	 * Set  : the set of parts that make up the match*/
	private HashSet<AIG> map(HashSet<AIG> selectedParts, HashMap<String,LogicGate> toMatch){
		
		//For each incomplete matching
		for(String protein : toMatch.keySet()){
			LogicGate g = toMatch.get(protein);
			
			//Try to match it using any part from library
			for(SBGate sbGate : Library.getGatesWithOutput(protein)){
			    AIG libPart = sbGate.getAIG();
			    
				//Ensure orthogonality
				if(selectedParts.contains(libPart)){
					continue;
				}
				
//				//Ensure proteins match
				if(!libPart.getOutputGate().getProtein().equals(protein)){
					continue;
				}
				
				//Can this be libPart be matched?
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
					HashSet<AIG> s = map(selectedParts, toMatchNext);

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
		HashMap<String, LogicGate> nextMatches = new HashMap<String, LogicGate>();

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
							nextMatches = isMatching(childOther,childThis);
							//See if it matches downwards.
							if(nextMatches != null){
								//Found one match 
								match++;
								alreadyMatched = childOther;
								mergedMatches.putAll(nextMatches);
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
			if(atPos instanceof InputGate){
				//If proteins match - and are specified in the inMap of the AIG
				if(((InputGate) other).getProtein().equals(((InputGate) atPos).getProtein()) 
						&& graph.containsInputProtein(((InputGate) atPos).getProtein())){
					//nextMatches should be empty here
					assert(nextMatches.isEmpty());
					return nextMatches;
				}
				else{
					//Proteins do not match
					//TODO add functionality to translate proteins freely
					return null;
				}
			}
			else{
				//Continue from this node using other's protein 
				nextMatches.put(((InputGate) other).getProtein(), atPos);
				return nextMatches;
			}
			
//			if(!(atPos instanceof InputGate && other.toString().equals(atPos.toString()) )){
//				//Otherwise store position of graph where to resume matching next part
//				//If not wrong mappings! (Orthogonality ensures we can abort here)
//				if(graph.inMap.containsKey(atPos.toString())){
//					return null;
//				} else {
//					nextMatches.put(other.toString(),atPos);
//				}
//			}
//			return nextMatches;
		
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
