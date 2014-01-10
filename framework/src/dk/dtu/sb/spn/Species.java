package dk.dtu.sb.spn;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Species {

    private String id;
    
    private String name;
    
    private List<Reaction> asProduct = new ArrayList<Reaction>();
    
    private List<Reaction> asReactant = new ArrayList<Reaction>();
    
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
     * @return
     */
    public String getId() {
        return id;
    }
    
    /**
     * 
     * @param reaction
     */
    public void addAsProductReaction(Reaction reaction) {
        asProduct.add(reaction);
    }
    
    /**
     * 
     * @return
     */
    public List<Reaction> asProductReactions() {
        return asProduct;
    }
    
    /**
     * 
     * @param reaction
     */
    public void addAsReactantReaction(Reaction reaction) {
        asReactant.add(reaction);
    }
    
    /**
     * 
     * @return
     */
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
    
    @Override
    public String toString() {
        return "<" + id +  ", " + name + ">";
    }
}
