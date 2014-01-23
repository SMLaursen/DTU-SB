package dk.dtu.ls.library;

import dk.dtu.ls.library.models.SBGate;

public class ConcreteParts {
    
    private static int cost(int repressors, int activators) {
        int cost = 0;
        if (repressors > 0) {
            cost += Math.pow(2, 1 / repressors);
        }
        if (activators > 0) {
            cost += Math.pow(2, 1 / activators);
        }
        return cost; 
    }
    
    private static String[] array(String... protein) {
        return protein;
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
                array(""), 
                "CI", 
                "CI = (aTc' Ara ) + (aTc Ara' )", 
                4000
                ));
        
        /** --------------------------------------------------------------------
         * OR device
         * Steady at ~4000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                2, 
                "2_or.xml", 
                cost(0, 1), 
                array("aTc", "Ara"),
                array(""), 
                "CI", 
                "CI = (aTc' Ara ) + (aTc Ara' )", 
                4000
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
                array(""), 
                "Ara", 
                "Ara = (IPTG lacI )", 
                3000
                ));
        
        /** --------------------------------------------------------------------
         * INV device
         * Steady at ~3000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                4, 
                "4_inv.xml", 
                cost(0, 1), 
                array("GFP"),
                array(""), 
                "aTc", 
                "aTc = (GFP' )", 
                3000
                ));
        
        /** --------------------------------------------------------------------
         * INV device
         * Steady at ~3000s
         * 1 promoter
         */
        Library.insert(new SBGate(
                5, 
                "5_inv.xml", 
                cost(0, 1), 
                array("TetR"),
                array(""), 
                "GFP", 
                "GFP = (TetR' )", 
                3000
                ));
    }
}
