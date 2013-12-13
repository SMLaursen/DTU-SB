package dk.dtu.sb.spn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.ASTNode;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnparsableExpressionException;
import dk.dtu.sb.Util;

/**
 * 
 */
public class RateFunction {

    /**
     * This rate is used when the rate doesn't depend on any concentrations.
     * Defaults to 1.0.
     */
    private double constantRate = 1.0;

    /**
     * The math formula with variable names. The built-in operators and function
     * can be seen here: <a
     * href="http://www.objecthunter.net/exp4j/">http://www.
     * objecthunter.net/exp4j/</a>
     */
    private String formula;

    /**
     * The variables in the formula.
     */
    private List<String> unknowns = new ArrayList<String>();

    /**
     * The object created from the variables in {@link #unknowns} and the
     * formula {@link #formula}.
     */
    private Calculable calc;

    /**
     * To avoid recalculations with the same variable values, we cache each
     * calculation.
     */
    private Map<String, Double> cache = new HashMap<String, Double>();

    /**
     * Constructor for constant rates.
     * 
     * @param constant
     */
    public RateFunction(double constant) {
        this.constantRate = constant;
    }

    /**
     * Constructor for rate function from formula with no variables.
     * 
     * @param formula
     *            See {@link #formula}.
     */
    public RateFunction(String formula) {
        this(formula, null);
    }

    /**
     * Constructor for rate function from formula with variables. Will try to
     * calculate immediately, if the calculation is successful it will assume
     * that there were no variables and in the future the {@link #constantRate}
     * will be return by {@link #getRate(Map)}.
     * 
     * @param formula
     *            See {@link #formula}.
     * @param unknowns
     *            A list of the variables in the formula.
     */
    public RateFunction(String formula, List<String> unknowns) {
        ExpressionBuilder expr = new ExpressionBuilder(formula);
        if (unknowns != null) {
            this.unknowns.addAll(unknowns);
        }
        this.formula = formula;

        try {
            this.constantRate = expr.build().calculate();
        } catch (Exception e) {
            Util.log.debug("Tried to calculate a constant rate, but some variables were missing. Fallback to always calculating the rate in getRate(). "
                    + e.getMessage());
            setCalc(expr);
        }
    }

    /**
     * Constructor for rate function given as MathML.
     * 
     * @param math
     *            See {@link ASTNode}.
     * @param unknowns
     *            A list of the variables in the MathML.
     */
    public RateFunction(ASTNode math, List<String> unknowns) {
        this(math.toFormula(), unknowns);
    }

    /**
     * Set all variables in the expression to 1.0 and build the
     * {@link Calculable} object.
     * 
     * @param expr
     *            See {@link ExpressionBuilder}.
     */
    private void setCalc(ExpressionBuilder expr) {
        for (String unknown : unknowns) {
            expr.withVariable(unknown, 1.0);
        }
        try {
            this.calc = expr.build();
        } catch (Exception e) {
            Util.log.error("An error occurred when building the Calculable object. Using default. "
                    + e.getMessage());
        }
    }

    /**
     * If {@link #calc} is not set we assume the {@link #constantRate} is used.
     * Else the variables in {@link #calc} is set to the values given in the
     * vars parameter.
     * 
     * @param vars
     *            A map with the current values of the variables.
     * @return The rate calculated or the {@link #constantRate}.
     */
    public synchronized double getRate(Map<String, Integer> vars) {
        if (calc != null) {
            try {
                String key = setVariablesAndGetKey(vars);
                if (!cache.containsKey(key)) {
                    double result = calc.calculate();
                    cache.put(key, result);
                    return result;
                } else {
                    return cache.get(key);
                }
            } catch (Exception e) {
                Util.log.error("An error occurred. Using default. " + e.getMessage());
            }
        }

        return this.constantRate;
    }

    /**
     * Sets the variables in {@link #calc} and creates a unique cache key.
     * 
     * @param vars
     *            The map passed to {@link #getRate(Map)}.
     * @return The unique key based on the variables used from vars. The format
     *         of the key will be: "K1=V1,K2=V2,..."
     * @throws UnparsableExpressionException
     */
    private String setVariablesAndGetKey(Map<String, Integer> vars)
            throws UnparsableExpressionException {
        StringBuilder keyBuilder = new StringBuilder();
        for (String unknown : unknowns) {
            if (vars.containsKey(unknown)) {
                keyBuilder.append(unknown);
                keyBuilder.append("=");
                keyBuilder.append(vars.get(unknown));
                keyBuilder.append(",");
                calc.setVariable(unknown, vars.get(unknown));
            } else {
                throw new UnparsableExpressionException("The variable "
                        + unknown
                        + " is missing. Cannot calculate the expression "
                        + this.formula);
            }
        }
        return keyBuilder.toString();
    }

    public String toString() {
        if (this.formula != null) {
            return this.formula;
        }
        return "" + this.constantRate;
    }
}
