package test.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sbml.jsbml.ASTNode;

import test.StdOutTester;
import dk.dtu.sb.spn.RateFunction;

public class RateFunctionTest extends StdOutTester {

    @Test
    public void testSimpleConstant() {
        RateFunction rate = new RateFunction(1.33);
        assertEquals(1.33, rate.getRate(null), 0);
    }
    
    @Test
    public void testSimpleConstantFunction() {
        RateFunction rate = new RateFunction("1/2");
        assertEquals(0.5, rate.getRate(null), 0);
    }
    
    @Test
    public void testSimpleVariableFunction() {
        List<String> unknowns = new ArrayList<String>();
        unknowns.add("x");
        RateFunction rate = new RateFunction("1/x", unknowns);
        Map<String, Integer> vars = new HashMap<String, Integer>();
        vars.put("x", 4);
        assertEquals(0.25, rate.getRate(vars), 0);
    }
    
    @Test
    public void testSimpleVariableFunction2() {
        List<String> unknowns = new ArrayList<String>();
        unknowns.add("x");
        RateFunction rate = new RateFunction("1/x", unknowns);
        Map<String, Integer> vars = new HashMap<String, Integer>();
        vars.put("x", 4);
        assertEquals(0.25, rate.getRate(vars), 0);
        
        vars.put("x", 5);
        assertEquals(0.2, rate.getRate(vars), 0);
    }
    
    @Test
    public void testSimpleVariableFunctionMissingVar() {
        List<String> unknowns = new ArrayList<String>();
        unknowns.add("x");
        RateFunction rate = new RateFunction("1/x", unknowns);
        Map<String, Integer> vars = new HashMap<String, Integer>();
        
        assertEquals(1, rate.getRate(vars), 0);
        
        assertTrue(err.toString().contains("An error occurred. Using default. The variable x is missing. Cannot calculate the expression 1/x"));
    }
    
    @Test
    public void testMathMLExpression() {
        ASTNode math = new ASTNode(4.0);
        math.divideBy(new ASTNode(8.0));
        
        RateFunction rate = new RateFunction(math, null);
        
        assertEquals(0.5, rate.getRate(null), 0);
    }
    
    @Test
    public void testWrongExpression() {
        new RateFunction("func(2)");
        assertTrue(err.toString().contains("An error occurred when building the Calculable object. Using default. Unable to parse character 'f' at position 1 in expression 'func(2)'"));
    }

}
