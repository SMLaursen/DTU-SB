package test.spn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.Species;
import dk.dtu.sb.data.StochasticPetriNet;

public class SPNtest {

	@Test
	public void test() {
		StochasticPetriNet spn = new StochasticPetriNet();
		
		// species
		spn.addSpecies(new Species("A"));
		spn.addSpecies(new Species("B"));
		spn.addSpecies(new Species("AB"));
		
		//Reaction 1
		Reaction r1 = new Reaction("Composition",0.9);
		r1.addReactant("A");
		r1.addReactant("B");
		r1.addProduct("AB");
		spn.addReaction(r1);
		
		//Reaction 2
		Reaction r2 = new Reaction("Decomposition",0.6);
		r2.addReactant("AB");
		r2.addProduct("A");
		r2.addProduct("B");
		spn.addReaction(r2);
		
		// Set initial markings
		spn.setInitialMarking("A", 1);
		spn.setInitialMarking("B", 1);
		spn.setInitialMarking("AB", 0);
		
		assertTrue(spn.getReaction("Composition").getProducts().containsKey("AB"));
		assertTrue(spn.getReaction("Composition").getReactants().containsKey("A"));
		assertTrue(spn.getReaction("Composition").getReactants().containsKey("B"));
		
		assertTrue(spn.getReaction("Decomposition").getReactants().containsKey("AB"));
		assertTrue(spn.getReaction("Decomposition").getProducts().containsKey("A"));
		assertTrue(spn.getReaction("Decomposition").getProducts().containsKey("B"));
		
		System.out.println(spn.toGraphviz());
		
		r1.removeReactant("B");
		assertFalse(spn.getReaction("Composition").getReactants().containsKey("B"));
		
		r2.removeProduct("A");
		assertFalse(spn.getReaction("Decomposition").getProducts().containsKey("A"));
		
		spn.removeReaction("Composition");
		assertNull(spn.getReaction("Composition"));
		
		
	}

}
