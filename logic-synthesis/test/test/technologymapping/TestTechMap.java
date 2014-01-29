package test.technologymapping;

import java.util.HashSet;

import org.junit.Test;

import dk.dtu.techmap.AIG;
import dk.dtu.techmap.TechnologyMapper;

public class TestTechMap {
	
	@Test
	public void test1(){
		AIG primary = new AIG("O = (C') + (A B)");
		
		AIG part1 = new AIG("O = (I) + (C')","Part1");
		AIG part2 = new AIG("I = (B A)","Part2");
		AIG part3 = new AIG("I = (Q)","Part1");
		AIG part4 = new AIG("O = (C') + (A B)");
		AIG part5 = new AIG("Q = (A B)");
		
		//Create library
		HashSet<AIG> library = new HashSet<AIG>();
		library.add(part1);
		library.add(part2);
		library.add(part3);
//		library.add(part4);
//		library.add(part5);
		
		//Define technology mapper on primary part
		TechnologyMapper techmap = new TechnologyMapper(primary);
		
		HashSet<AIG> solution = techmap.start(library);
		System.out.println(solution);
	}
	
	
}
