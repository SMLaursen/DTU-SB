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
        System.out.println(f);
        f.reduceToPrimeImplicants();
        System.out.println(f);
        f.reducePrimeImplicantsToSubset();
        System.out.println(f);
    }
    
    @Test
    public void testSimpleTT() throws IOException {
        Formula f = Formula.readCompleteTT(new BufferedReader(new FileReader("test/test/qmc/input_tt.txt")));
        f.reduceToPrimeImplicants();
        f.reducePrimeImplicantsToSubset();
        System.out.println(f);
    }

}
