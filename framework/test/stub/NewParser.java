package stub;

import dk.dtu.sb.parser.AbstractParser;
import dk.dtu.sb.spn.StochasticPetriNet;

public class NewParser extends AbstractParser {

    @Override
    public StochasticPetriNet parse() {
        return spn;
    }
}
