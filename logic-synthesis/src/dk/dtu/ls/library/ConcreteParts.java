package dk.dtu.ls.library;

import java.util.ArrayList;
import java.util.Arrays;

public class ConcreteParts {
    
    public static int cost(int repressors, int activators) {
        int cost = 0;
        if (repressors > 0) {
            cost += Math.pow(2, 1 / repressors);
        }
        if (activators > 0) {
            cost += Math.pow(2, 1 / activators);
        }
        return cost; 
    }
    
    public static ArrayList<String> array(String... protein) {
        return new ArrayList<String>(Arrays.asList(protein));
    }

    public static void insertParts() {

        /** --------------------------------------------------------------------
         * OR device
         * Steady at ~4000s
         * 2 promoters
         */
        Library.insert(new SBGate(
                1, 
                "1_or.xml", 
                cost(0, 2), 
                array("aTc", "Ara"),
                array(), 
                "CI", 
                "CI = (aTc ) + (Ara )", 
                1000
                ));
        
        /** --------------------------------------------------------------------
         * OR device
         * Steady at ~1000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                2, 
                "2_or.xml", 
                cost(0, 1), 
                array("aTc", "Ara"),
                array(), 
                "CI", 
                "CI = (aTc ) + (Ara )", 
                1000
                ));
        
        /** --------------------------------------------------------------------
         * AND device
         * Steady at ~3000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                3, 
                "3_and.xml", 
                cost(0, 1), 
                array("IPTG", "lacI"),
                array(), 
                "Ara", 
                "Ara = (IPTG lacI )", 
                1000
                ));
        
        /** --------------------------------------------------------------------
         * INV device
         * Steady at ~3000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                4, 
                "4_inv.xml", 
                cost(1, 0), 
                array("GFP"),
                array(), 
                "aTc", 
                "aTc = (GFP' )", 
                1000
                ));
        
        /** --------------------------------------------------------------------
         * INV device
         * Steady at ~700s
         * 1 promoter
         */
        Library.insert(new SBGate(
                5, 
                "5_inv.xml", 
                cost(1, 0), 
                array("TetR"),
                array(), 
                "GFP", 
                "GFP = (TetR' )", 
                700
                ));
        
        /** --------------------------------------------------------------------
         * NOR device
         * Steady at ~800s
         * 1 promoter
         */
        Library.insert(new SBGate(
                6, 
                "6_nor.xml", 
                cost(1, 2), 
                array("Ara", "aTc"),
                array("CI"), 
                "YFP", 
                "YFP = (Ara' aTc' )", 
                800
                ));
    }
}
