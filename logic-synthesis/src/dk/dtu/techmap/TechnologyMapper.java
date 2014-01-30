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
	public HashSet<SBGate> start(){
		HashMap<String, LogicGate> startingGate = new HashMap<String, LogicGate>();
		startingGate.put(graph.getOutputGate().getProtein(), graph.getOutputGate());
		HashSet<SBGate> solution = new HashSet<SBGate>();
		return map(solution, startingGate);
	}

	/** Maps using the library of {@link AIG}'s
	 * 
	 * @return
	 * empty : no match could be found
	 * Set  : the set of parts that make up the match*/
	private HashSet<SBGate> map(HashSet<SBGate> selectedParts, HashMap<String,LogicGate> toMatch){

		HashSet<SBGate> allSelectedParts = new HashSet<SBGate>();
		allSelectedParts.addAll(selectedParts);
		
		//For each incomplete matching
		for(String protein : toMatch.keySet()){
			LogicGate g = toMatch.get(protein);
			Boolean foundSubSolution = false; 
			
			//Try to match it using any part from library with matching proteins
			for(SBGate sbGate : Library.getGatesWithOutput(protein)){
				AIG libPart = sbGate.getAIG();

				//Ensure orthogonality
				if(allSelectedParts.contains(sbGate)){
					continue;
				}

				//TODO Ensure complete orthogonality by checking all intermediate proteins

				//Can this be libPart be matched?
				HashMap<String, LogicGate> toMatchNext = isMatching(libPart.getOutputGate(), g);

				//Part didn't match. skip this part.
				if(toMatchNext==null){
					continue;
				}//Entire sub graph has been matched! 
				else if(toMatchNext.isEmpty()){
					//Record score / Output solution
					allSelectedParts.add(sbGate);
					return allSelectedParts;
				}//Further matching should be conducted  
				else {
					//Add this to selected parts
					allSelectedParts.add(sbGate);			

					//Recursively match remainder
					HashSet<SBGate> s = map(allSelectedParts, toMatchNext);

					//If this branch didn't lead to a solution, remove part again and try matching using next part. 
					if(s.isEmpty()){
						allSelectedParts.remove(libPart);
						continue;
					} else {
						allSelectedParts.addAll(s);
						foundSubSolution = true;
						break;
					}					
				}
			}
			//If gate could not be matched
			if(!foundSubSolution){
				allSelectedParts.clear();
				return allSelectedParts;
			}
		
		}

		return allSelectedParts;

	}

	/** Checks whether 'other' matches the subtree at 'atPos' 
	 * @return
	 * null : incompatible library part.
	 * empty map : compatible library part that can cover the entire remaining AIG. 
	 * non-empty map : compatible library part - but further matching is necessary. 
	 * */
	public HashMap<String, LogicGate> isMatching(LogicGate other, LogicGate atPos){
	
		if(other instanceof AndGate && atPos instanceof AndGate){

			//Map for storing intersections
			HashMap<String, LogicGate> mergedMatches = new HashMap<String, LogicGate>();

			//Sanity Checks
			assert(other.in.size() == 2);
			assert(atPos.in.size() == 2);

			LogicGate thisChild1 = (LogicGate) atPos.in.toArray()[0];
			LogicGate thisChild2 = (LogicGate) atPos.in.toArray()[1];
			LogicGate otherChild1 = (LogicGate) other.in.toArray()[0];
			LogicGate otherChild2 = (LogicGate) other.in.toArray()[1];

			HashMap<String, LogicGate> nextMap1 = new HashMap<String, LogicGate>();
			HashMap<String, LogicGate> nextMap2 = new HashMap<String, LogicGate>();

			//Try combination 1:
			nextMap1 = isMatching(otherChild1,thisChild1);
			if(nextMap1 != null){
				nextMap2 = isMatching(otherChild2,thisChild2);
				if(nextMap2 != null){
					mergedMatches.putAll(nextMap1);
					mergedMatches.putAll(nextMap2);
					return mergedMatches;
				}
			}
			//Try combination 2:
			nextMap1 = isMatching(otherChild1,thisChild2);
			if(nextMap1 != null){
				nextMap2 = isMatching(otherChild2,thisChild1);
				if(nextMap2 != null){
					mergedMatches.putAll(nextMap1);
					mergedMatches.putAll(nextMap2);
					return mergedMatches;
				}
			}
			//No matches has been found
			return null;

		}//TODO Some problems can occur here! 
		else if(other instanceof NotGate && atPos instanceof NotGate){
			//Sanity checks
			assert(other.in.size() == 1);
			assert(atPos.in.size() == 1);
			//Recurse on both children
			LogicGate childOther = (LogicGate) other.in.toArray()[0];
			LogicGate childThis = (LogicGate) atPos.in.toArray()[0];
			return isMatching(childOther, childThis);

		} else if(other instanceof InputGate){
			HashMap<String, LogicGate> nextMatches = new HashMap<String, LogicGate>();
			if(atPos instanceof InputGate && graph.containsInputProtein(((InputGate) other).getProtein())){
				//If proteins match - and are specified in the inMap of the AIG
				if(((InputGate) other).getProtein().equals(((InputGate) atPos).getProtein())){
					//nextMatches should be empty here
					assert(nextMatches.isEmpty());
					return nextMatches;
				} 
				else{
					//Proteins do not match
					return null;
				}
			}else{
				//Continue from this node using other's protein 
				nextMatches.put(((InputGate) other).getProtein(), atPos);
				return nextMatches;
			}
		} else if(other instanceof OutputGate){
			//Sanity check
			assert(other.in.size() == 1);
			//Proceed to next node
			LogicGate childOther = (LogicGate) other.in.toArray()[0];
			LogicGate childThis = atPos;
			//If also a OutputGate (Only happens on the first match)
			if(atPos instanceof OutputGate){
				assert(atPos.in.size() == 1);
				childThis = (LogicGate) atPos.in.toArray()[0];
			}
			return isMatching(childOther, childThis);

		} else {
			return null;
		}
	}
}
