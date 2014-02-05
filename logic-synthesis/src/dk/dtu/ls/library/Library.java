package dk.dtu.ls.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Library {
    
    public static final String KEY_ID = "ID";
    public static final String KEY_SBML_FILE = "SBML_FILE";
    public static final String KEY_REPRESSORS = "REPRESSORS";
    public static final String KEY_ACTIVATORS = "ACTIVATORS";
    public static final String KEY_INPUT = "INPUT";
    public static final String KEY_INTERMEDIATE = "INTERMEDIATE";
    public static final String KEY_OUTPUT = "OUTPUT";
    public static final String KEY_SOP = "SOP";
    public static final String KEY_STEADY = "STEADY";

    private static HashMap<String, List<SBGate>> parts = new HashMap<String, List<SBGate>>();
    
    public static HashSet<String> array(String... protein) {
        return new HashSet<String>(Arrays.asList(protein));
    }

    public static void clear() {
        parts.clear();
    }

    public static void loadLibrary(String path) {
        clear();

        File folder = new File(path + "library");
        
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".properties")) {
                Library.insert(SBGate.loadFromProperties(file.getAbsolutePath()));
            }
        }
    }
    
    public static void loadLibrary() {
        loadLibrary("");
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
        List<SBGate> gates = parts.containsKey(gate.outputProtein) ? parts
                .get(gate.outputProtein) : new ArrayList<SBGate>();

        gates.add(gate);
        Collections.sort(gates);

        parts.put(gate.outputProtein, gates);
    }

    public static void remove(SBGate gate) {
        if (parts.containsKey(gate.outputProtein)) {
            List<SBGate> gates = parts.get(gate.outputProtein);
            gates.remove(gate);
        }
    }

    public static List<SBGate> getGatesWithOutput(String outputProtein) {
        return parts.containsKey(outputProtein) ? parts.get(outputProtein)
                : new ArrayList<SBGate>();
    }

    public static List<SBGate> getAllParts() {
        ArrayList<SBGate> allParts = new ArrayList<SBGate>();
        for (List<SBGate> gates : parts.values()) {
            allParts.addAll(gates);
        }
        return allParts;
    }
}
