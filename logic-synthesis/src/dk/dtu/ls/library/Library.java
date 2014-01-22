package dk.dtu.ls.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dk.dtu.ls.library.models.SBGate;

public class Library {

    private static HashMap<String, List<SBGate>> parts = new HashMap<String, List<SBGate>>();
    
    public static void clear() {
        parts.clear();
    }
    
    public static void insert(SBGate gate) {
        List<SBGate> gates = parts.containsKey(gate.outputProtein) ? parts.get(gate.outputProtein) : new ArrayList<SBGate>();
        
        gates.add(gate);
        Collections.sort(gates);
        
        parts.put(gate.outputProtein, gates);
    }
    
    public static List<SBGate> getGatesWithOutput(String outputProtein) {
        return parts.get(outputProtein);
    }
}
