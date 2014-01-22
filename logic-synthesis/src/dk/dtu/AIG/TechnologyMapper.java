package dk.dtu.AIG;

import java.util.HashMap;

import com.github.qtstc.Formula;

public class TechnologyMapper {
	OutputGate output;
	HashMap<String,InputGate> inMap = new HashMap<String,InputGate>();
	boolean isAIG = false;
	
	/**Parses formula f into the AIG structure */
	public TechnologyMapper(Formula f){
			output = parseFormula(f);
			convertToAIG(output);
			//Map to DeviceNodes using DataBase
			//Translate DeviceNodes to a PN
			//Simulate PN
	}
	
	
	
	/** Parses the String representation of Formula f into a graph of (2 input) AND, OR and NOT 
	 * @throws Exception */
	public OutputGate parseFormula(Formula f){
		String text = f.toString(); 
		
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
				if(prev_temp.out.in.size() != 1){
					throw new RuntimeException("Graph is inconsistent : multiple input to inverter");
				}
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
					if(child.in.size() != 1){
						throw new RuntimeException("Graph is inconsistent : multiple input to inverter");
					}
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
	
	/** Checks whether other can be placed at atPos */
	public boolean isMatching(TechnologyMapper other, LogicGate atPos){
		
		return true;
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
