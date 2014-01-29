package dk.dtu.techmap;

import java.util.HashMap;
import java.util.Map;

/** Class to represent the corresponding device interconnect*/
public class DeviceNode {
	String device_name;
	Map<String, Container> in;
	Map<String, Container> out;
	
	public DeviceNode(String name){
		device_name = name;
		in = new HashMap<String, Container>();
		out = new HashMap<String, Container>();
	}
}

/** Container class for maintaining edges in the device graph 
 * while pointing to the corresponding Gates in the AIG */
class Container{
	DeviceNode node;
	LogicGate gate;
}



