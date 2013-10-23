package dk.dtu.sb.parser;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import dk.dtu.sb.data.StochasticPetriNet;

public class SBMLParser extends Parser {
    
    Model model;
    SBMLReader reader = new SBMLReader();
    
    public void readFile(String filename) throws XMLStreamException, IOException {
        SBMLDocument document = reader.readSBMLFromFile(filename);
        model = document.getModel();
    }
    
    public StochasticPetriNet parseToSPN() {
        
                
        return this.spn;
    }

}
