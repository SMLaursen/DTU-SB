package dk.dtu.sb.parser;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import dk.dtu.sb.Util;
import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.StochasticPetriNet;

/**
 * A parser for SBML. The different elements in the SBML will be parsed as follows:
 * <p>
 *  Reaction -> Transition <br/>
 *  Reactant -> Place<br/>
 *  Product  -> Place<br/>
 *  Modifier -> Place
 * </p>
 */
public class SBMLParser extends Parser {
    
    Model model;
    SBMLReader reader = new SBMLReader();
    
    /**
     * {@inheritDoc}
     */
    public StochasticPetriNet parse() {
        try {
            SBMLDocument document = reader.readSBMLFromString(input);
            model = document.getModel();
            
            parseReactions();
            
            parseMarkings();
            
        } catch (XMLStreamException e) {
            Util.log.fatal("An error occurred when parsing the SBML file.", e);
        }
                
        return this.spn;
    }
    
    private void parseReactions() {
        for (org.sbml.jsbml.Reaction r : model.getListOfReactions()) {
            // r.getKineticLaw();
            String reactionName = !r.getName().isEmpty() ? r.getName() : r.getId();
            Reaction newReaction = new Reaction(reactionName, 1.0);
            
            for (ModifierSpeciesReference sr : r.getListOfModifiers()) {
                newReaction.addReactant(sr.getSpecies());
            }
            
            for (SpeciesReference sr : r.getListOfReactants()) {
                newReaction.addReactant(sr.getSpecies());
            }
            
            for (SpeciesReference sr : r.getListOfProducts()) {
                newReaction.addProduct(sr.getSpecies());
            }
                        
            spn.addReaction(newReaction);
        }
    }
    
    private void parseMarkings() {
        for (Species s : model.getListOfSpecies()) {
            spn.setInitialMarkings(s.getId(), (int)s.getInitialAmount());
        }
    }

}
