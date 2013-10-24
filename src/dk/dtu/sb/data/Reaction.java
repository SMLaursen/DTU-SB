package dk.dtu.sb.data;

import java.util.HashMap;
import java.util.Map;

public class Reaction {
	private String name;
	private Map<String, Integer> reactants;
	private Map<String, Integer> products;
	private double rate;
	
	public Reaction(String name, double rate){
		this.name = name;
		reactants = new HashMap<String,Integer>();
		products = new HashMap<String,Integer>();
		this.rate = rate;
	}
	
	public void addReactant(String reactant, int multiplicity){
		reactants.put(reactant,multiplicity);
	}
	public void addReactant(String reactant){
		addReactant(reactant, 1);
	}
	public int removeReactant(String reactant){
		return reactants.remove(reactant);
	}
	public Map<String, Integer> getReactants(){
		return reactants;
	}
	
	public void addProduct(String product, int multiplicity){
		products.put(product,multiplicity);
	}
	public void addProduct(String product){
		addProduct(product, 1);
	}
	public int removeProduct(String product){
		return products.remove(product);
	}
	public Map<String, Integer> getProducts(){
		return products;
	}
	
	public String getName(){
		return name;
	}
	public double getRate(){
		return rate;
	}
	
	public String toString(){
		String s ="id : "+name +", rate : "+rate+"\n";
		s+= "  Reactants :"+ reactants+"\n"; 
		s+= "  Products  :"+ products+"\n"; 
		return s;
	}
}
