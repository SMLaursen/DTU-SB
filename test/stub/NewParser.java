package stub;

import dk.dtu.sb.parser.Parser;
import dk.dtu.sb.spn.StochasticPetriNet;

public class NewParser extends Parser {

    @Override
    public StochasticPetriNet parse() {
        return spn;
    }
}
