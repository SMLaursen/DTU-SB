package dk.dtu.sb.data;

public class Reactant extends Species {

    public Reactant(String id, String name, int multiplicity) {
        super(id, name, multiplicity);
    }
    
    public Reactant(String id, int multiplicity) {
        super(id, id, multiplicity);
    }
    
    public Reactant(String id, String name) {
        super(id, name, 1);
    }
    
    public Reactant(String id) {
        super(id, id, 1);
    }

}
