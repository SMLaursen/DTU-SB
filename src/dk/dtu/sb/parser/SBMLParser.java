package dk.dtu.sb.parser;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.StochasticPetriNet;

public class SBMLParser extends Parser {
    
    Model model;
    SBMLReader reader = new SBMLReader();
    
    public void readFile(String filename) throws XMLStreamException, IOException {
        SBMLDocument document = reader.readSBMLFromFile(filename);
        model = document.getModel();
    }
    
    public StochasticPetriNet parse() {
        for (org.sbml.jsbml.Reaction r : model.getListOfReactions()) {
            // r.getKineticLaw();
            Reaction newReaction = new Reaction(r.getId(), 1.0);
            for (SpeciesReference sr : r.getListOfReactants()) {
                newReaction.addReactant(sr.getSpecies());
            }
            for (SpeciesReference sr : r.getListOfProducts()) {
                newReaction.addProduct(sr.getSpecies());
            }
            spn.addReaction(newReaction);
        }
        
        for (Species s : model.getListOfSpecies()) {
            spn.setInitialMarkings(s.getId(), (int)s.getInitialAmount());
        }
        
        return this.spn;
    }

}
