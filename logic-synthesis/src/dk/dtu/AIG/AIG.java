package dk.dtu.AIG;

import java.util.HashMap;
import com.github.qtstc.Formula;

public class AIG {
	OutputGate output;
	HashMap<String,InputGate> inMap = new HashMap<String,InputGate>();
	
	/**Parses formula f into the AIG structure */
	public AIG(Formula f){
		String text = f.toString(); 
		System.out.println(text);
		
		//Get index of equals
		int index = text.indexOf("=");
		
		//Get output protein
		String out = text.substring(0, index).trim();
//		System.out.println(out);
		text = text.substring(index+1).trim();
		
		//Get terms
		String[] terms = text.replaceAll("[\\(\\)]","").split("\\+");
	
		output = new OutputGate(out);
		Gate or = new OrGate();
		output.addChild(or);
		for(String s : terms){	
			//Add and gates
			Gate g = new AndGate();
			or.addChild(g);
			//Connect to InputGates - possibly with a NotGate
			String[] term = s.trim().split("[ ]");
			for(String t : term){
				boolean inverted = t.endsWith("'");
				String literal = t.replace("'", "").trim();
				if(!inMap.containsKey(literal)){
					inMap.put(literal, new InputGate(literal));
				}else {
					System.out.println("Warning : complete orthogonality is not preserved");
				}	
				InputGate input = inMap.get(literal);
				if(inverted){
					//Add a notgate on the way to the inputgate
					Gate inv = new NotGate();
					g.addChild(inv);	
					inv.addChild(input);
				} else {
					g.addChild(input);
				}
					
			}
		}
	}
	
	/**Creates an empty AIG for manual tree building*/
	public AIG(){
		
	}
	
	public String toString(){
		return output.subTreeToString();
	}
}
