package dk.dtu.ls.library;

import dk.dtu.ls.library.models.SBGate;

public class ConcreteParts {

        public static void insertParts() {

        /**
         * New part
         */
        Library.insert(new SBGate(
                0, 
                "test.xml", 
                0, 
                new String[] { "in" },
                new String[] { "interme" }, 
                "out", 
                "GFP = ()", 
                100
                ));
    }
}
