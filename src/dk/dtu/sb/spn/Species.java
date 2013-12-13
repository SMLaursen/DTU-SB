package dk.dtu.sb.spn;

import java.util.ArrayList;
import java.util.List;

public class Species {

    private String id;
    
    private String name;
    
    private List<Reaction> asProduct = new ArrayList<Reaction>();
    
    private List<Reaction> asReactant = new ArrayList<Reaction>();
    
    public Species(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Species(String id) {
        this(id, id);
    }
    
    public String getId() {
        return id;
    }
    
    public void addAsProductReaction(Reaction reaction) {
        asProduct.add(reaction);
    }
    
    public List<Reaction> asProductReactions() {
        return asProduct;
    }
    
    public void addAsReactantReaction(Reaction reaction) {
        asReactant.add(reaction);
    }
    
    public List<Reaction> asReactantReactions() {
        return asReactant;
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
        return "<" + id +  ", " + name + ">";
    }
}
