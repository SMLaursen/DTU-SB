package test.technologymapping;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import dk.dtu.techmap.AIG;
import dk.dtu.techmap.TechnologyMapper;

public class TestTechMap {

	AIG primary = new AIG("O = (C') + (A B)");

	AIG part1 = new AIG("O = (I) + (C')","Part1");
	AIG part2 = new AIG("I = (B A)","Part2");
	AIG part3 = new AIG("I = (Q)","Part3");
	AIG part4 = new AIG("O = (C') + (A B)","Part4");
	AIG part5 = new AIG("Q = (A B)","Part5");

	@Test
	public void test1(){	
		//Test that proteins have to match
		
		HashSet<AIG> library = new HashSet<AIG>();
		//Test that p1 and p5 cannot be a solution
		library.add(part1);
		library.add(part5);
		TechnologyMapper techmap = new TechnologyMapper(primary);
		HashSet<AIG> solution = techmap.start(library);
		assertTrue(solution==null);
		
		//Test that p1 and p2 can be a solution
		library.add(part2);
		solution = techmap.start(library);
		assertTrue(solution!=null);
		
		//Test that p1,p3 and p5 can be a solution. (ADVANCED : p3 has to bee mapped for "free")
		library.add(part3);
		library.remove(part2);
		solution = techmap.start(library);
		assertTrue(solution!=null);
		
		//Test that p4 can be a solution
		library.add(part4);
		library.remove(part1);
		solution = techmap.start(library);
		assertTrue(solution != null);
		
		//Test that the previous tries didn't cause any side-effects
		library.remove(part3);
		library.remove(part4);
		solution = techmap.start(library);
		assertTrue(solution == null);	
	}

	@Test
	public void test2(){
		//Create library
		HashSet<AIG> library = new HashSet<AIG>();
		library.add(part1);
		//				library.add(part2);
		library.add(part3);
		//				library.add(part4);
		library.add(part5);
		//Define technology mapper on primary part
		TechnologyMapper techmap = new TechnologyMapper(primary);
		HashSet<AIG> solution = techmap.start(library);
		System.out.println(solution);
	}


}
