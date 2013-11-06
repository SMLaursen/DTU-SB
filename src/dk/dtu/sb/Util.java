package dk.dtu.sb;

import java.util.HashMap;

import org.apache.commons.logging.impl.SimpleLog;

import dk.dtu.sb.data.Product;
import dk.dtu.sb.data.Reactant;
import dk.dtu.sb.data.Reaction;

public class Util {

    /**
     * 
     */
    public static SimpleLog log = new SimpleLog("DTU-SB");

    /**
     * Executes the reaction and updates the markings with r
     * 
     * @param reaction
     * @param previousMarkings
     */
    public static void updateMarkings(Reaction reaction,
            HashMap<String, Integer> previousMarkings) {
        // Reactants
        for (Reactant reactant : reaction.getReactants().values()) {
            int multiplicity = reactant.getMultiplicity();
            int old = previousMarkings.get(reactant.getId());

            if (old < multiplicity) {
                throw new RuntimeException(
                        "ERROR : performing update with fewer tokens than required");
            }
            // Overwrite old value with updated
            previousMarkings.put(reactant.getId(), old - multiplicity);
        }

        // Products
        for (Product product : reaction.getProducts().values()) {
            int multiplicity = product.getMultiplicity();
            int old = previousMarkings.get(product.getId());
            
            // Overwrite old value with updated
            previousMarkings.put(product.getId(), old + multiplicity);
        }
    }

}