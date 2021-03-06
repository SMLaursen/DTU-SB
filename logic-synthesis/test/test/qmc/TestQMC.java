package test.qmc;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.github.qtstc.Formula;

public class TestQMC {

    @Test
    public void testSimple() throws IOException {
        Formula f = Formula.read(new BufferedReader(new FileReader("test/test/qmc/input.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        assertEquals("GFP = (Lacl' GalR' ) + (TetR GalR )", f.toString());
    }
    
    @Test
    public void testSimpleTT() throws IOException {
        Formula f = Formula.readCompleteTT(new BufferedReader(new FileReader("test/test/qmc/input_tt_xor3.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        assertEquals("GFP = (Lacl' TetR' CI ) + (Lacl' TetR CI' ) + (Lacl TetR' CI' ) + (Lacl TetR CI )", f.toString());
    }
    
    @Test
    public void testLibraryTT() throws IOException {
        Formula f = Formula.readCompleteTT(new BufferedReader(new FileReader("test/test/qmc/input_tt.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        System.out.println(f);
    }

}
