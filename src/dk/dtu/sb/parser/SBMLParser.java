package dk.dtu.sb.parser;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.test.gui.JSBMLvisualizer;

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
    
    SBMLDocument document;
    Model model;
    SBMLReader reader = new SBMLReader();
    
    /**
     * {@inheritDoc}
     */
    public StochasticPetriNet parse() {
        if (parseFile()) {
            parseReactions();
            parseMarkings();
        }
        
        return this.spn;
    }
    
    /**
     * Parse the SBML file.
     * 
     * @return Whether the parse was successful or not.
     */
    private boolean parseFile() {
        if (document == null || model == null) {
            try {
                document = reader.readSBMLFromString(getInput());
                model = document.getModel();
                return true;
            } catch (XMLStreamException e) {
                Util.log.fatal("An error occurred when parsing the SBML file: " + e.getMessage() + ". Expect the StochasticPetriNet to be incomplete.");
            }
        }
        return false;
    }
    
    private void parseReactions() {
        for (org.sbml.jsbml.Reaction reaction : model.getListOfReactions()) {
            
            String reactionName = !reaction.getName().isEmpty() ? reaction.getName() : reaction.getId();
            Reaction newReaction = new Reaction(reactionName, 1.0);
            
            for (ModifierSpeciesReference sr : reaction.getListOfModifiers()) {
                newReaction.addReactant(sr.getSpecies());
            }
            
            for (SpeciesReference sr : reaction.getListOfReactants()) {
                newReaction.addReactant(sr.getSpecies());
            }
            
            for (SpeciesReference sr : reaction.getListOfProducts()) {
                newReaction.addProduct(sr.getSpecies());
            }
                        
            spn.addReaction(newReaction);
        }
    }
    
    private void parseMarkings() {
        for (Species specie : model.getListOfSpecies()) {
            spn.setInitialMarkings(specie.getId(), (int)specie.getInitialAmount());
        }
    }

}
