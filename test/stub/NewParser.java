package stub;

import dk.dtu.sb.data.StochasticPetriNet;
import dk.dtu.sb.parser.Parser;

public class NewParser extends Parser {

    @Override
    public StochasticPetriNet parse() {
        return spn;
    }

    @Override
    public void readFile(String filename) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
