package dk.dtu.techmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Util;

public class TechnologyMapper {
	private AIG graph;
	private HashSet<HashSet<SBGate>> solutions = new HashSet<HashSet<SBGate>>();

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
	public void start(){
	    solutions.clear();
		HashMap<String, LogicGate> startingGate = new HashMap<String, LogicGate>();
		startingGate.put(graph.getOutputGate().getProtein(), graph.getOutputGate());
		HashSet<SBGate> solution = new HashSet<SBGate>();
		Util.log.debug(graph.treeToString());
		map(solution, startingGate, true);
	}
	
	public ArrayList<HashSet<SBGate>> getSolutions() {
	    return new ArrayList<HashSet<SBGate>>(solutions);
	}

	/** Maps using the library of {@link AIG}'s
	 * 
	 * @return
	 * empty : no match could be found
	 * Set  : the set of parts that make up the match*/
	private HashSet<SBGate> map(HashSet<SBGate> selectedParts, HashMap<String,LogicGate> toMatch, boolean root){

		//Make a copy of the currently selected parts
		HashSet<SBGate> allSelectedParts = new HashSet<SBGate>();
		allSelectedParts.addAll(selectedParts);

		//Figure out all the proteins already in use (To ensure orthogonality)
		HashSet<String> allUsedProteins = new HashSet<String>();		

		//For each incomplete matching
		for(String protein : toMatch.keySet()){
			LogicGate g = toMatch.get(protein);
			Boolean foundSubSolution = false; 
		
			//Try to match it using any part from library with matching proteins
			for(SBGate sbGate : Library.getGatesWithOutput(protein)){
				AIG libPart = sbGate.getAIG();
				
				//Add all currently used proteins
				allUsedProteins.clear();
				for(SBGate s : allSelectedParts){
					allUsedProteins.addAll(s.intermediateProteins);
					allUsedProteins.addAll(s.inputProteins);
				}	
				//Ensure orthogonality
				if(allSelectedParts.contains(sbGate) ||
				   isOverLapping(sbGate.inputProteins,allUsedProteins) ||
				   isOverLapping(sbGate.intermediateProteins,allUsedProteins))
				   {
					continue;
				}
				//Can this be libPart be matched?
				for(HashMap<String, LogicGate> toMatchNext : isMatching(libPart.getOutputGate(), g)){
					//					System.out.println(libPart+"  "+toMatchNext);
					//Part didn't match. skip this part and try a new.
					if(toMatchNext==null){
						continue;
					}//Entire sub graph has been matched! 
					else if(toMatchNext.isEmpty()){
						//Record score / Output solution
						allSelectedParts.add(sbGate);
						foundSubSolution = true;
						if (root) {
						    solutions.add((HashSet<SBGate>) allSelectedParts.clone());
						    allSelectedParts.remove(sbGate);
						}
						break;
					}//Further matching should be conducted  
					else {
						//Add this to selected parts
						allSelectedParts.add(sbGate);			

						//Recursively match remainder using remaining parts
						HashSet<SBGate> remainder = map(allSelectedParts, toMatchNext, false);

						//If this branch didn't lead to a solution, remove part again (So that it may be reused later)
						//and try matching using next part. 
						if(remainder.isEmpty()){
							allSelectedParts.remove(sbGate);
							continue;
						} else {
							allSelectedParts.addAll(remainder);
							foundSubSolution = true;
							solutions.add((HashSet<SBGate>) allSelectedParts.clone());
							//allSelectedParts.clear();
							allSelectedParts.removeAll(remainder);
							break;
						}					
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

	//Checks whether set a and b overlaps
	private boolean isOverLapping(HashSet<String> a, HashSet<String> b){
		for(String s : a){
			if(b.contains(s)){
				return true;
			}
		}
		return false;
	}

	/** Checks whether 'otherPart' matches thisPart at the subtree 'thisPartPos' 
	 * @return
	 * empty list : incompatible library part. 
	 * non-empty list : compatible library part where 
	 * If list-map is empty finished matching.
	 * if list-map is non-empty further matching necessary. 
	 * */
	public LinkedList<HashMap<String, LogicGate>> isMatching(LogicGate otherPart, LogicGate thisPartPos){

		if(otherPart instanceof AndGate && thisPartPos instanceof AndGate){
			//Map for storing intersections

			LinkedList<HashMap<String,LogicGate>> returnList = new LinkedList<HashMap<String,LogicGate>>();

			//Sanity Checks
			assert(otherPart.in.size() == 2);
			assert(thisPartPos.in.size() == 2);

			LogicGate thisChild1 = (LogicGate) thisPartPos.in.toArray()[0];
			LogicGate thisChild2 = (LogicGate) thisPartPos.in.toArray()[1];
			LogicGate otherChild1 = (LogicGate) otherPart.in.toArray()[0];
			LogicGate otherChild2 = (LogicGate) otherPart.in.toArray()[1];

			//TODO Nested AND gates (If graph was larger than 2-AIG initially 
			//Can require us to iterate over entire list instead of peekFirst

			//Try all permutations of combination 1:
			for(HashMap<String, LogicGate> nextMap1 : isMatching(otherChild1,thisChild1)){
				for(HashMap<String, LogicGate> nextMap2 : isMatching(otherChild2,thisChild2)){
					HashMap<String, LogicGate> mergedMatches = new HashMap<String, LogicGate>();
					mergedMatches.putAll(nextMap1);
					mergedMatches.putAll(nextMap2);
					returnList.add(mergedMatches);
				}
			}

			//Try all permutations of combination 1:
			for(HashMap<String, LogicGate> nextMap1 : isMatching(otherChild1,thisChild2)){
				for(HashMap<String, LogicGate> nextMap2 : isMatching(otherChild2,thisChild1)){
					HashMap<String, LogicGate> mergedMatches = new HashMap<String, LogicGate>();
					mergedMatches.putAll(nextMap1);
					mergedMatches.putAll(nextMap2);
					returnList.add(mergedMatches);
				}
			}

			//No matches has been found
			return returnList;

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
			LinkedList<HashMap<String, LogicGate>> set = new LinkedList<HashMap<String, LogicGate>>();
			HashMap<String, LogicGate> nextMatches = new HashMap<String, LogicGate>();
			//If type signature match and protein are specified in the inMap of the AIG
			if(thisPartPos instanceof InputGate && graph.containsInputProtein(((InputGate) otherPart).getProtein())){
				//Ensure proteins really match
				if(((InputGate) otherPart).getProtein().equals(((InputGate) thisPartPos).getProtein())){
					//nextMatches are empty here
					set.add(nextMatches);
				} 
			}else{
				//Continue matching from thisPartPos node using other's protein 
				nextMatches.put(((InputGate) otherPart).getProtein(), thisPartPos);
				set.add(nextMatches);
			}
			return set;


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
			return new LinkedList<HashMap<String,LogicGate>>();
		}
	}
}
