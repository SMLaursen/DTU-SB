package dk.dtu.AIG;

import java.util.HashMap;

import com.github.qtstc.Formula;

public class TechnologyMapper {
	LogicGate output;
	//Map with inputs, this map is emptied as matches are found
	HashMap<String,InputGate> inMap = new HashMap<String,InputGate>();
	//Stores the LogicGates that needs to be matched next by the protein .
	HashMap<String, LogicGate> resumeProgress = new HashMap<String, LogicGate>();
	boolean isAIG = false;

	/**Parses formula f into the AIG structure */
	public TechnologyMapper(Formula f){
		output = parseFormula(f.toString());
		convertToAIG(output);
		//STRASH 
		//Map to DeviceNodes using DataBase
		//Translate DeviceNodes to a PN
		//Simulate PN
	}
	
	public TechnologyMapper(String f){
		output = parseFormula(f);
		convertToAIG(output);
	}
	
	public HashMap<String,LogicGate> getProgress(){
		return resumeProgress;
	}
	/**When the entire graph has been mapped, the inMap is empty.*/
	public boolean isMapped(){
		return inMap.isEmpty();
	}

	/** Parses the String representation of Formula f into a graph of (2 input) AND, OR and NOT 
	 * @throws Exception */
	public OutputGate parseFormula(String text){
		
		//Get index of equals
		int index = text.indexOf("=");

		//Get output protein
		String out = text.substring(0, index).trim();
		text = text.substring(index+1).trim();

		//Get terms
		String[] terms = text.replaceAll("[\\(\\)]","").split("\\+");
		OutputGate output = new OutputGate(out);
		//Figure out whether to put a OR-gate
		LogicGate topGate;
		if(terms.length > 2){
			//TODO : Support this
			throw new RuntimeException("This version only supports 2 terms per formula");
		} else if (terms.length == 2){
			topGate = new OrGate();
			output.addChild(topGate);
		} else {
			//Skip the topgate if only 1 term
			topGate = output;
		}
		for(String s : terms){	
			String[] term = s.trim().split("[ ]");
			//Figure out whether to put AND-gates
			LogicGate midGate;
			if(term.length > 2){
				//TODO : Support this
				throw new RuntimeException("This version only supports 2 literals pr. term");
			} else if (term.length == 2) {
				//Add and gates
				midGate = new AndGate();
				topGate.addChild(midGate);
			} else{
				//Skip the midgate
				midGate = topGate;
			}
			for(String t : term){
				//Do not create duplicate input gates
				boolean inverted = t.endsWith("'");
				String literal = t.replace("'", "").trim();
				if(!inMap.containsKey(literal)){
					inMap.put(literal, new InputGate(literal));
				}else {
					System.out.println("Warning : complete orthogonality is not preserved");
				}	
				//Connect to InputGates - possibly with a NotGate
				InputGate input = inMap.get(literal);
				if(inverted){
					//Add a notgate on the way to the inputgate
					LogicGate inv = new NotGate();
					midGate.addChild(inv);	
					inv.addChild(input);
				} else {
					//Connect directly
					midGate.addChild(input);
				}

			}
		}
		return output;
	}

	/**Converts the CNF graph to AIG-representation*/
	public LogicGate convertToAIG(LogicGate curr){
		if(curr instanceof OrGate){
			LogicGate curr_temp = new AndGate();
			LogicGate prev_temp = curr.out;
			//Add inverter to incoming edge :
			//If already inverter on incoming edge
			if(prev_temp instanceof NotGate){
				//Graph is inconsistent : multiple input to inverter
				assert(prev_temp.out.in.size() != 1);

				//The additional inverter corresponds to removing the current
				LogicGate prevprev_temp = prev_temp.out;
				prevprev_temp.removeChild(prev_temp);
				prevprev_temp.addChild(curr_temp);
			}
			//If some other gate on incoming edge
			else{
				LogicGate not = new NotGate();
				prev_temp.removeChild(curr);
				prev_temp.addChild(not);
				not.addChild(curr_temp);
			}
			//Add inverters on child edges
			for(LogicGate child : curr.in){
				if(child instanceof NotGate){
					//"Graph is inconsistent : multiple input to inverter"
					assert(child.in.size() == 1);
					//Ensured that only one childchild exists
					for(LogicGate childchild : child.in){
						curr_temp.addChild(childchild);
					}
				} else {
					LogicGate notGate = new NotGate();
					curr_temp.addChild(notGate);
					notGate.addChild(child);
				}
			}
			curr = curr_temp;
		}
		//Recursively traverse remaining
		for(LogicGate child : curr.in){
			convertToAIG(child);
		}
		isAIG = true;
		return null;
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
				if(inMap.containsKey(atPos.toString())){
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
	

	public LogicGate getOutputGate(){
		return output;
	}

	/** Returns a copy of the tree rooting from g*/
	public LogicGate CopyTree(OutputGate g){
		LogicGate copy = new OutputGate(g.protein);
		//TODO
		return copy;
	}

	public String toString(){
		return output.subTreeToString();
	}
}
