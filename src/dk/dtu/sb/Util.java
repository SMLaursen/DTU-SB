package dk.dtu.sb;

import java.util.HashMap;

import org.apache.commons.logging.impl.SimpleLog;

import dk.dtu.sb.data.Reaction;

public class Util {
    
    public static SimpleLog log = new SimpleLog("DTU-SB");
    
    /** Executes the reaction and updates the markings with r*/
	public static void updateMarkings(Reaction r,HashMap<String,Integer> prev){
		//Reactants
		for(String s : r.getReactants().keySet()){
			int multiplicity = r.getReactants().get(s);
			int old = prev.get(s);
			
			if(old < multiplicity){
				throw new RuntimeException("ERROR : performing update with fewer tokens than required");
			}
			//Overwrite old value with updated
			prev.put(s, old-multiplicity);
		}
		//Products
		for(String s : r.getProducts().keySet()){
			int multiplicity = r.getProducts().get(s);
			int old = prev.get(s);
			//Overwrite old value with updated
			prev.put(s, old+multiplicity);
		}
	}
    
}