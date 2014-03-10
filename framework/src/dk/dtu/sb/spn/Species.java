package dk.dtu.sb.spn;

/**
 * Representing a species with an ID and name.
 */
public class Species {

    private String id;    
    private String name;
    
    /**
     * 
     * @param id
     * @param name
     */
    public Species(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * 
     * @param id
     */
    public Species(String id) {
        this(id, id);
    }
    
    /**
     * 
     */
    public String getId() {
        return id;
    }
        
    /**
     * Can be used to represent the species in e.g. graphs.
     * 
     * @return {@link #name} if present, else {@link #id}. 
     */
    public String getLabel() {
        return name != null && !name.trim().isEmpty() ? name : id;
    }
    
    @Override
    public String toString() {
        return "<" + id +  ", " + name + ">";
    }
}
