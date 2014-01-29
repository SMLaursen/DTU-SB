package dk.dtu.techmap;

import java.util.HashMap;

import com.github.qtstc.Formula;

public class TechnologyMapper {
	
	//Stores the LogicGates that needs to be matched next by the protein .
	HashMap<String, LogicGate> resumeProgress = new HashMap<String, LogicGate>();

	AIG graph;
	
	/**Parses formula f into the AIG structure */
	public TechnologyMapper(AIG g){
		//Ensure AIG has been correctly defined.
		assert(g.output instanceof OutputGate);
		graph = g;
	}
	
	public HashMap<String,LogicGate> getProgress(){
		return resumeProgress;
	}


	/** Checks whether 'other' matches the subtree at 'atPos' */
	public boolean isMatching(LogicGate other, LogicGate atPos){
		if(other instanceof AndGate){
			if(atPos instanceof AndGate){
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
							//See if it matches downwards.
							if(isMatching(childOther,childThis)){
								//Found one match 
								match++;
								alreadyMatched = childOther;
								//If both branches could be matched, return true.
								if(match == 2){
									return true;
								}
								//Else break (And check the other branch)
								break;
							}
							//Else clear any partial matches made so far.
							resumeProgress.clear();
						}
					}
				}
				//return false when everything has been explored and clear progress.
				return false;
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
			return false;
		} else if(other instanceof InputGate){
			//If the input proteins matches, we have a "complete" match.
			if(!(atPos instanceof InputGate && other.toString().equals(atPos.toString()) )){
				//Otherwise store position of graph where to resume matching next part
				//If not wrong mappings! (Orthogonality ensures we can abort here)
				if(graph.inMap.containsKey(atPos.toString())){
					return false;
				} else {
					resumeProgress.put(other.toString(), atPos);
				}
			}
			return true;
		} else if(other instanceof OutputGate){
			resumeProgress.clear();
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
		//Shouldnt be here
		assert(false);
		return false;
	}
}
