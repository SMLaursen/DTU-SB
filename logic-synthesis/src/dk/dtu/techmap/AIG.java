package dk.dtu.techmap;

import java.util.HashMap;

import dk.dtu.sb.Util;

/**
 * Represents And-Inverter-Graph
 */
public class AIG {
    private String name;
    private OutputGate output;

    // Map with inputs, this map is emptied as matches are found
    private HashMap<String, InputGate> inMap = new HashMap<String, InputGate>();

    public AIG(String formula) {
        this(formula, null);
    }

    public AIG(String formula, String name) {
        output = parseFormula(formula);
        Util.log.debug("Before Convert2AIG: " + output.subTreeToString());
        convert2AIG(output);
        Util.log.debug("AIG: " + output.subTreeToString());
        this.name = (name == null) ? output.subTreeToString() : name;
    }

    /**
     * Parses the String representation of Formula f into a graph of (N input)
     * AND, OR and NOT
     */
    private OutputGate parseFormula(String formula) {

        int indexOfEquals = formula.indexOf("=");

        // Get output protein
        String out = formula.substring(0, indexOfEquals).trim();
        OutputGate output = new OutputGate(out);

        // Get terms
        formula = formula.substring(indexOfEquals + 1).trim();
        String[] terms = formula.replaceAll("[\\(\\)]", "").split("\\+");

        LogicGate topGate;

        // Figure out whether to put a OR-gate
        if (terms.length > 1) {
            topGate = new OrGate();
            output.addChild(topGate);
        } else {
            topGate = output;
        }

        for (String term : terms) {
            String[] literals = term.trim().split("[ ]");
            LogicGate midGate;

            // Figure out whether to put AND-gates
            if (literals.length > 1) {
                midGate = new AndGate();
                topGate.addChild(midGate);
            } else {
                midGate = topGate;
            }
            for (String literal : literals) {
                // Do not create duplicate input gates
                boolean inverted = literal.trim().endsWith("'");
                literal = literal.replace("'", "").trim();

                if (!inMap.containsKey(literal)) {
                    inMap.put(literal, new InputGate(literal));
                } else {
                    Util.log.warn("Complete orthogonality is not preserved");
                }

                // Connect to InputGates - possibly with a NotGate
                InputGate input = inMap.get(literal);
                if (inverted) {
                    // Add a notgate on the way to the inputgate
                    LogicGate inv = new NotGate();
                    midGate.addChild(inv);
                    inv.addChild(input);
                } else {
                    // Connect directly
                    midGate.addChild(input);
                }

            }
        }
        return output;
    }

    /**
     * Converts the N-CNF graph to a single 2-AIG-representation. Many different
     * representation can exists.
     */
    private void convert2AIG(LogicGate curr) {

        if (curr instanceof OrGate) {
            LogicGate curr_temp = new AndGate();
            LogicGate prev_temp = curr.out;
            // Add inverter to incoming edge :
            // If already inverter on incoming edge
            /*if (prev_temp instanceof NotGate) {
                // Graph is inconsistent : multiple input to inverter
                assert (prev_temp.out.in.size() != 1);

                // The additional inverter corresponds to removing the current
                LogicGate prevprev_temp = prev_temp.out;
                prevprev_temp.removeChild(prev_temp);
                prevprev_temp.addChild(curr_temp);
            }
            // If some other gate on incoming edge
            else {*/
                LogicGate not = new NotGate();
                prev_temp.removeChild(curr);
                prev_temp.addChild(not);
                not.addChild(curr_temp);
            //}
            // Add inverters on child edges
            for (LogicGate child : curr.in) {
                /*if (child instanceof NotGate) {
                    // "Graph is inconsistent : multiple input to inverter"
                    assert (child.in.size() == 1);
                    // Ensured that only one childchild exists
                    for (LogicGate childchild : child.in) {
                        curr_temp.addChild(childchild);
                    }
                } else {*/
                    LogicGate notGate = new NotGate();
                    curr_temp.addChild(notGate);
                    notGate.addChild(child);
                //}
            }
            curr = curr_temp;
            // Call on self (Converts
            convert2AIG(curr);

        } else if (curr instanceof AndGate && curr.in.size() > 2) {
            int noOfChildren = curr.in.size();

            // Convert N-AND gates to 2-AND gates
            if (noOfChildren == 3) {
                // Replace by two And-Gates
                LogicGate curr_temp1 = new AndGate();
                LogicGate curr_temp2 = new AndGate();

                Object[] children = curr.in.toArray();

                curr_temp1.out = curr.out;
                curr_temp1.in.add((LogicGate) children[0]);
                curr_temp1.in.add(curr_temp2);

                curr_temp2.out = curr_temp1;
                curr_temp2.in.add((LogicGate) children[1]);
                curr_temp2.in.add((LogicGate) children[2]);

                LogicGate curr_parent = curr.out;
                curr_parent.removeChild(curr);
                curr_parent.addChild(curr_temp1);
                convert2AIG(curr_temp1);
            } else {
                Util.log.error("No more than 3 input supported");
                throw new RuntimeException(
                        "Error : No more than 3 input supported");
            }
        } else {
            // Recursively traverse remaining
            for (LogicGate child : curr.in) {
                convert2AIG(child);
            }
        }

    }

    @Override
    public String toString() {
        return name;
    }

    public OutputGate getOutputGate() {
        return output;
    }

    public String getOutputProtein() {
        return output.getProtein();
    }

    public boolean containsInputProtein(String protein) {
        return inMap.containsKey(protein);
    }

    /**
     * When the entire graph has been mapped, the inMap is empty.
     */
    public boolean isMapped() {
        return inMap.isEmpty();
    }

    public String treeToString() {
        return output.subTreeToString();
    }
}
