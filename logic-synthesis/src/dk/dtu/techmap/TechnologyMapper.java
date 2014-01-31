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

		//Add top-level node
		graph = g;
	}

	/** Starts mapping.
	 * @return
	 * The set of parts in the solution. Empty for no solution. */
	public HashSet<SBGate> start(){
		HashMap<String, LogicGate> startingGate = new HashMap<String, LogicGate>();
		startingGate.put(graph.getOutputGate().getProtein(), graph.getOutputGate());
		HashSet<SBGate> solution = new HashSet<SBGate>();
		System.out.println(graph.treeToString());
		return map(solution, startingGate);
	}

	/** Maps using the library of {@link AIG}'s
	 * 
	 * @return
	 * empty : no match could be found
	 * Set  : the set of parts that make up the match*/
	private HashSet<SBGate> map(HashSet<SBGate> selectedParts, HashMap<String,LogicGate> toMatch){
	
		//Make a copy of the currently selected parts
		HashSet<SBGate> allSelectedParts = new HashSet<SBGate>();
		allSelectedParts.addAll(selectedParts);
		
		//For each incomplete matching
		for(String protein : toMatch.keySet()){
			LogicGate g = toMatch.get(protein);
			Boolean foundSubSolution = false; 
			
			//Try to match it using any part from library with matching proteins
			for(SBGate sbGate : Library.getGatesWithOutput(protein)){
				AIG libPart = sbGate.getAIG();
				
				//If we've already used that part once.
				if(allSelectedParts.contains(sbGate)){
					continue;
				}
				//TODO Ensure complete orthogonality by checking all intermediate proteins
				
				//Can this be libPart be matched?
				HashMap<String, LogicGate> toMatchNext = isMatching(libPart.getOutputGate(), g);
				System.out.println(libPart+"  "+toMatchNext);
				//Part didn't match. skip this part and try a new.
				if(toMatchNext==null){
					continue;
				}//Entire sub graph has been matched! 
				else if(toMatchNext.isEmpty()){
					//Record score / Output solution
					allSelectedParts.add(sbGate);
					foundSubSolution = true;
					break;
				}//Further matching should be conducted  
				else {
					//Add this to selected parts
					allSelectedParts.add(sbGate);			

					//Recursively match remainder using remaining parts
					HashSet<SBGate> remainder = map(allSelectedParts, toMatchNext);

					//If this branch didn't lead to a solution, remove part again (So that it may be reused later)
					//and try matching using next part. 
					if(remainder.isEmpty()){
						allSelectedParts.remove(libPart);
						continue;
					} else {
						allSelectedParts.addAll(remainder);
						foundSubSolution = true;
						break;
					}					
				}
			}
			//If gate could not be matched - return empty set (i.e. no match possible)
			if(!foundSubSolution){
				allSelectedParts.clear();
				return allSelectedParts;
			}
		
		}
		return allSelectedParts;
	}

	/** Checks whether 'otherPart' matches thisPart at the subtree 'thisPartPos' 
	 * @return
	 * null : incompatible library part.
	 * empty map : compatible library part that can cover the entire remaining AIG. 
	 * non-empty map : compatible library part - but further matching is necessary. 
	 * */
	public HashMap<String, LogicGate> isMatching(LogicGate otherPart, LogicGate thisPartPos){
	
		if(otherPart instanceof AndGate && thisPartPos instanceof AndGate){
			//Map for storing intersections
			HashMap<String, LogicGate> mergedMatches = new HashMap<String, LogicGate>();

			//Sanity Checks
			assert(otherPart.in.size() == 2);
			assert(thisPartPos.in.size() == 2);

			LogicGate thisChild1 = (LogicGate) thisPartPos.in.toArray()[0];
			LogicGate thisChild2 = (LogicGate) thisPartPos.in.toArray()[1];
			LogicGate otherChild1 = (LogicGate) otherPart.in.toArray()[0];
			LogicGate otherChild2 = (LogicGate) otherPart.in.toArray()[1];

			HashMap<String, LogicGate> nextMap1 = new HashMap<String, LogicGate>();
			HashMap<String, LogicGate> nextMap2 = new HashMap<String, LogicGate>();

			//TODO there might be cases where both combinations can be true. 
			//Will that be a problem?
			boolean go = false;
			//Try combination 1:
			nextMap1 = isMatching(otherChild1,thisChild1);
			if(nextMap1 != null){
				nextMap2 = isMatching(otherChild2,thisChild2);
				if(nextMap2 != null){
					mergedMatches.putAll(nextMap1);
					mergedMatches.putAll(nextMap2);
//					go = true;
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
//					go = true;
					return mergedMatches;
				}
			}
//			if(go){
//				System.out.println(mergedMatches);
//				return mergedMatches;
//			}
			//No matches has been found
			return null;

			
		}//TODO Some problems can occur here near the input gates if inverted! 
		else if(otherPart instanceof NotGate && thisPartPos instanceof NotGate){
			//Sanity checks
			assert(otherPart.in.size() == 1);
			assert(thisPartPos.in.size() == 1);
			//Recurse on both children
			LogicGate childOther = (LogicGate) otherPart.in.toArray()[0];
			LogicGate childThis = (LogicGate) thisPartPos.in.toArray()[0];
			return isMatching(childOther, childThis);

			
		} else if(otherPart instanceof InputGate){
			HashMap<String, LogicGate> nextMatches = new HashMap<String, LogicGate>();
			//If type signature match and protein are specified in the inMap of the AIG
			if(thisPartPos instanceof InputGate && graph.containsInputProtein(((InputGate) otherPart).getProtein())){
				//Ensure proteins really match
				if(((InputGate) otherPart).getProtein().equals(((InputGate) thisPartPos).getProtein())){
					//nextMatches should be empty here
					assert(nextMatches.isEmpty());
					return nextMatches;
				} 
				else{
					//Proteins do not match
					return null;
				}
			}else{
				//Continue matching from thisPartPos node using other's protein 
				nextMatches.put(((InputGate) otherPart).getProtein(), thisPartPos);
				return nextMatches;
			}
			
			
		} else if(otherPart instanceof OutputGate){
			//Sanity check
			assert(otherPart.in.size() == 1);
			//Just proceed to next node "ignoring" the outputgate
			LogicGate childOther = (LogicGate) otherPart.in.toArray()[0];
			LogicGate childThis = thisPartPos;
			//If also a OutputGate (Only happens on the first match)
			if(thisPartPos instanceof OutputGate){
				assert(thisPartPos.in.size() == 1);
				childThis = (LogicGate) thisPartPos.in.toArray()[0];
			}
			return isMatching(childOther, childThis);
			
			
		} else {
			return null;
		}
	}
}
