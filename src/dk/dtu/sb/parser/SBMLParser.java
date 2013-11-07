package dk.dtu.sb.parser;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.KineticLaw;
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
 * A parser for SBML. The different elements in the SBML will be parsed as
 * follows:
 * <p>
 * Reaction -> Transition <br/>
 * Reactant -> Place<br/>
 * Product -> Place<br/>
 * Modifier -> Place
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
                Util.log.fatal("An error occurred when parsing the SBML file: "
                        + e.getMessage()
                        + ". Expect the StochasticPetriNet to be incomplete.");
            }
        }
        return false;
    }

    private void parseReactions() {
        for (org.sbml.jsbml.Reaction reaction : model.getListOfReactions()) {

            Reaction newReaction = translateReaction(reaction);

            for (ModifierSpeciesReference sr : reaction.getListOfModifiers()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addReactant(new dk.dtu.sb.data.Species(s.getId(), s.getName()));
            }

            for (SpeciesReference sr : reaction.getListOfReactants()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addReactant(new dk.dtu.sb.data.Species(s.getId(), s.getName()));
            }

            for (SpeciesReference sr : reaction.getListOfProducts()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addProduct(new dk.dtu.sb.data.Species(s.getId(), s.getName()));
            }

            spn.addReaction(newReaction);
        }
    }
    
    /**
     * Determines parameters for the reaction.
     * 
     * @param reaction
     * @return {@link Reaction}
     */
    private Reaction translateReaction(org.sbml.jsbml.Reaction reaction) {
        double rate = 1.0;
           
        if (reaction.getKineticLaw().getMath() != null) {
            for (ASTNode term : reaction.getKineticLaw().getMath().getChildren()) {
                if (term.isNumber()) {
                    rate = term.getReal();
                }
            }
        }
        
        return new Reaction(reaction.getId(), reaction.getName(), rate);
    }

    private void parseMarkings() {
        for (Species specie : model.getListOfSpecies()) {
            spn.setInitialMarking(specie.getId(),
                    (int) specie.getInitialAmount());
        }
    }

}
