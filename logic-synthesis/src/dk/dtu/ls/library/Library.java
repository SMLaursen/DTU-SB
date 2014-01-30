package dk.dtu.ls.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Library {

    private static HashMap<String, List<SBGate>> parts = new HashMap<String, List<SBGate>>();
    
    public static void clear() {
        parts.clear();
    }
    
    public static SBGate getById(int id) {
        for (List<SBGate> gates : parts.values()) {
            for (SBGate gate : gates) {
                if (gate.id == id) {
                    return gate;
                }
            }
        }
        return null;
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
    
    public static List<SBGate> getAllParts() {
        ArrayList<SBGate> allParts = new ArrayList<SBGate>();
        for (List<SBGate> gates : parts.values()) {
            allParts.addAll(gates);
        }
        return allParts;
    }
}
