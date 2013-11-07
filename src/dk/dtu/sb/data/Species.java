package dk.dtu.sb.data;

public class Species {

    private String id;
    
    private String name;
    
    private int multiplicity;
    
    public Species(String id, String name, int multiplicity) {
        this.id = id;
        this.name = name;
        this.multiplicity = multiplicity;
    }
    
    public Species(String id, int multiplicity) {
        this(id, id, multiplicity);
    }
    
    public Species(String id, String name) {
        this(id, name, 1);
    }
    
    public Species(String id) {
        this(id, id, 1);
    }
    
    public String getId() {
        return id;
    }
    
    public int getMultiplicity() {
        return multiplicity;
    }
    
    /**
     * Can be used to represent the species in e.g. graphs.
     * 
     * @return {@link #name} if present, else {@link #id}. 
     */
    public String getLabel() {
        return name != null && !name.trim().isEmpty() ? name : id;
    }
    
    public String toString() {
        return "<" + getLabel() +  ", " + multiplicity + ">";
    }
}
