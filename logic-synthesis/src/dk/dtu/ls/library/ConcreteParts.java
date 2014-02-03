package dk.dtu.ls.library;

import java.util.Arrays;
import java.util.HashSet;

public class ConcreteParts {
    
    public static HashSet<String> array(String... protein) {
        return new HashSet<String>(Arrays.asList(protein));
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
                0, 
                2, 
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
                0, 
                1, 
                array("aTc", "Ara"),
                array(), 
                "CI", 
                "CI = (aTc ) + (Ara )", 
//                "CI = (aTc ) + (IPTG lacl )",
//                "CI = (aTc ) + (IPTG lacl )",
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
                0, 
                1, 
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
                1, 
                0, 
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
                1, 
                0, 
                array("TetR"),
                array(), 
                "GFP", 
                "GFP = (TetR' )", 
                700
                ));
        
        /** --------------------------------------------------------------------
         * NOR device
         * Steady at ~800s
         * 3 promoter
         */
        Library.insert(new SBGate(
                6, 
                "6_nor.xml", 
                1, 
                2, 
                array("Ara", "aTc"),
                array("CI"), 
                "YFP", 
                "YFP = (Ara' aTc' )", 
                800
                ));
        
        /** --------------------------------------------------------------------
         * Converter
         * Steady at ~500s
         * 1 promoter
         */
        Library.insert(new SBGate(
                7, 
                "7_conv.xml", 
                0, 
                1, 
                array("GFP"),
                array(), 
                "aTc", 
                "aTc = (GFP )", 
                500
                ));
        
        /** --------------------------------------------------------------------
         * or with not
         * Steady at ~500s
         * 2 promoters
         */
        Library.insert(new SBGate(
                8, 
                "8_or_not.xml", 
                1, 
                1, 
                array("GFP", "Ara"),
                array(), 
                "CI", 
                "CI = (GFP') + (Ara)", 
                500
                ));
    }
}
