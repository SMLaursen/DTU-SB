package dk.dtu.techmap;

import java.util.HashSet;
import java.util.Set;

public abstract class LogicGate {

    protected static final String TYPE_AND = "And";
    protected static final String TYPE_OR = "Or";
    protected static final String TYPE_NOT = "Not";
    protected static final String TYPE_IN = "In";
    protected static final String TYPE_OUT = "Out";

    protected Set<LogicGate> in;
    protected LogicGate out;
    private static int idCounter = 0;
    protected int myId;

    protected String type;

    protected LogicGate() {
        in = new HashSet<LogicGate>();
        out = null;
        myId = idCounter++;
    }

    protected void addChild(LogicGate g) {
        // Sanity check
        assert (g != null);
        in.add(g);
        g.out = this;
    }

    /**
     * Recursively prints all nodes in preorder-lisp notation from this Gate as
     * root
     */
    public String subTreeToString() {
        StringBuffer output = new StringBuffer();
        output.append("(");
        for (LogicGate gate : in) {
            output.append(gate.toString());
        }
        output.append(")");
        return output.toString();
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        if (this.type.equals(TYPE_OUT)) {
            output.append(((OutputGate) this).getProtein());
            output.append(" = ");
            output.append(subTreeToString());
        } else if (this.type.equals(TYPE_IN)) {
            output.append(((InputGate) this).getProtein());
            output.append("()");
        } else {
            output.append(this.type);
            output.append(subTreeToString());
        }
        return output.toString();
    }

    /**
     * Removes the child object
     */
    protected void removeChild(LogicGate g) {
        in.remove(g);
        g.out = null;
    }

    protected int getId() {
        return myId;
    }
}

class AndGate extends LogicGate {
    public AndGate() {
        super();
        this.type = TYPE_AND;
    }
}

class OrGate extends LogicGate {
    public OrGate() {
        super();
        this.type = TYPE_OR;
    }
}

class NotGate extends LogicGate {
    public NotGate() {
        super();
        this.type = TYPE_NOT;
    }
}

class InputGate extends LogicGate {
    private String protein;

    public InputGate(String protein) {
        out = null;
        // Do net initialize in!
        this.protein = protein;
        this.type = TYPE_IN;
    }

    public String getProtein() {
        return protein;
    }
}

class OutputGate extends LogicGate {
    private String protein;

    public OutputGate(String protein) {
        super();
        this.protein = protein;
        this.type = TYPE_OUT;
    }

    public String getProtein() {
        return protein;
    }
}