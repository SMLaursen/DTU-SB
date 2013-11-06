package dk.dtu.sb.data;

public class Product extends Species {

    public Product(String id, String name, int multiplicity) {
        super(id, name, multiplicity);
    }
    
    public Product(String id, int multiplicity) {
        super(id, id, multiplicity);
    }
    
    public Product(String id, String name) {
        super(id, name, 1);
    }
    
    public Product(String id) {
        super(id, id, 1);
    }
}
