package dk.dtu.AIG;

import java.util.HashMap;

import com.github.qtstc.Formula;

public class AIG {
	OutputGate output;
	HashMap<String,InputGate> inMap = new HashMap<String,InputGate>();
	
	/**Parses formula f into the AIG structure */
	public AIG(Formula f){
		try {
			Gate g = parseFormula(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/**Creates an empty AIG for manual tree building*/
	//public Graph(){}
	
	/** Parses the String representation of Formula f into a graph of (n-fan in/out) AND, OR and NOT 
	 * @throws Exception */
	public Gate parseFormula(Formula f) throws Exception{
		String text = f.toString(); 
		
		//Get index of equals
		int index = text.indexOf("=");
		
		//Get output protein
		String out = text.substring(0, index).trim();
		text = text.substring(index+1).trim();
		
		//Get terms
		String[] terms = text.replaceAll("[\\(\\)]","").split("\\+");
		output = new OutputGate(out);
		//Figure out whether to put a OR-gate
		Gate topGate;
		if(terms.length > 2){
			throw new Exception("This release only supports 2 terms per formula");
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
			Gate midGate;
			if(term.length > 2){
				throw new Exception("This release only supports 2 literals pr. term");
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
					Gate inv = new NotGate();
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
	
	public String toString(){
		return output.subTreeToString();
	}
}
