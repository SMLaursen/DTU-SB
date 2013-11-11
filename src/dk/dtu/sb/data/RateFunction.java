package dk.dtu.sb.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.ASTNode;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dk.dtu.sb.Util;

/**
 * 
 */
public class RateFunction {

    private double constantRate = 1.0;
    private List<String> unknowns = new ArrayList<String>();
    private Calculable calc;
    private ASTNode original;
    private String formula = "";
    private Map<String, Double> cache = new HashMap<String, Double>();

    /**
     * Default constructor. Initialises with default values.
     */
    public RateFunction() {
    }

    /**
     * 
     * @param constant
     */
    public RateFunction(double constant) {
        this.constantRate = constant;
    }

    /**
     * 
     * @param formula
     * @param unknowns
     */
    public RateFunction(String formula, List<String> unknowns) {
        ExpressionBuilder expr = new ExpressionBuilder(formula);
        this.unknowns.addAll(unknowns);
        this.formula = formula;

        try {
            this.constantRate = expr.build().calculate();
        } catch (Exception e) {
            Util.log.debug("Tried to calculate a constant rate, but some variables were missing. Fallback to always calculating the rate in getRate(). "
                    + e.getMessage());

            for (String unknown : unknowns) {
                expr.withVariable(unknown, 1.0);
            }

            try {
                this.calc = expr.build();
            } catch (Exception e1) {
                Util.log.fatal("An error occurred. Using default. "
                        + e.getMessage());
            }
        }
    }

    /**
     * 
     * @param math
     * @param unknowns
     */
    public RateFunction(ASTNode math, List<String> unknowns) {
        this(math.toFormula(), unknowns);
        this.original = math;
    }

    /**
     * 
     * @param vars
     * @return
     */
    public double getRate(Map<String, Integer> vars) {
        if (this.calc != null) {
            try {
                StringBuilder keyBuilder = new StringBuilder();
                for (String unknown : unknowns) {
                    if (vars.containsKey(unknown)) {
                        keyBuilder.append(unknown);
                        keyBuilder.append("=");
                        keyBuilder.append(vars.get(unknown));
                        keyBuilder.append(",");
                        calc.setVariable(unknown, vars.get(unknown));
                    } else {
                        throw new UnparsableExpressionException(
                                "The variable "
                                        + unknown
                                        + " is missing. Cannot calculate the expression "
                                        + this.formula);
                    }
                }

                String key = keyBuilder.toString();
                if (!cache.containsKey(key)) {
                    cache.put(key, calc.calculate());
                }
                return cache.get(key);
            } catch (Exception e) {
                Util.log.fatal("An error occurred. Using default. "
                        + e.getMessage());
            }
        }

        return this.constantRate;
    }

    public String toString() {
        if (this.original != null) {
            return "" + this.original.toFormula();
        }
        return "" + this.constantRate;
    }
}
