package dk.dtu.sb.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.ASTNode;

import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnparsableExpressionException;
import dk.dtu.sb.Util;

/**
 * 
 */
public class RateFunction {

    private double constantRate = 1.0;
    private List<String> unknowns = new ArrayList<String>();
    private ExpressionBuilder expr;
    private ASTNode original;
    private String formula = "";

    /**
     * Default constructor. Initialises with default values.
     */
    public RateFunction() {}

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
        this.expr = new ExpressionBuilder(formula);
        this.unknowns.addAll(unknowns);
        this.formula = formula;

        try {
            this.constantRate = this.expr.build().calculate();
            this.expr = null;
        } catch (Exception e) {
            Util.log.debug("Tried to calculate a constant rate, but some variables were missing. Fallback to always calculating the rate in getRate(). "
                    + e.getMessage());
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
        if (this.expr != null) {
            try {
                for (String unknown : unknowns) {
                    if (vars.containsKey(unknown)) {
                        expr.withVariable(unknown, vars.get(unknown));
                    } else {
                        throw new UnparsableExpressionException(
                                "The variable "
                                        + unknown
                                        + " is missing. Cannot calculate the expression "
                                        + this.formula);
                    }
                }
                return expr.build().calculate();
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
