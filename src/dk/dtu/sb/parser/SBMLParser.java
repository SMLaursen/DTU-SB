package dk.dtu.sb.parser;

import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.InitialAssignment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
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
            parseSpecies();
            parseReactions();
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

    /**
     * Set the transitions in the SPN.
     */
    private void parseReactions() {
        for (org.sbml.jsbml.Reaction reaction : model.getListOfReactions()) {

            Reaction newReaction = translateReaction(reaction);

            for (ModifierSpeciesReference sr : reaction.getListOfModifiers()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addModifier(s.getId());
            }

            for (SpeciesReference sr : reaction.getListOfReactants()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addReactant(s.getId());
            }

            for (SpeciesReference sr : reaction.getListOfProducts()) {
                Species s = sr.getSpeciesInstance();
                newReaction.addProduct(s.getId());
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

        double rate = getConstant(reaction.getKineticLaw().getMath(), 1.0);

        return new Reaction(reaction.getId(), reaction.getName(), rate);
    }

    /**
     * Sets the initial markings of the SPN.
     */
    private void parseSpecies() {
        for (Species specie : model.getListOfSpecies()) {
            spn.addSpecies(new dk.dtu.sb.data.Species(specie.getId(), specie
                    .getName()));
            spn.setInitialMarking(specie.getId(),
                    (int) specie.getInitialAmount());
        }

        for (InitialAssignment ia : model.getListOfInitialAssignments()) {
            int marking = (int) getConstant(ia.getMath(),
                    spn.getInitialMarking(ia.getVariable()));

            spn.setInitialMarking(ia.getVariable(), marking);
        }
    }

    /**
     * Traverses the MathML object and finds a constant to use.
     * 
     * @param math
     *            {@link ASTNode}.
     * @param def
     *            Default constant.
     * @return The found constant or the default.
     */
    private double getConstant(ASTNode math, double def) {
        double constant = def;

        if (math != null) {
            if (math.getChildCount() > 0) {

                ExpressionBuilder expr = new ExpressionBuilder(math.toFormula());

                // find constant in expression
                for (ASTNode term : math.getChildren()) {
                    if (term.isName()) {
                        expr.withVariable(term.getName(), 1.0);
                    }
                    if (term.isReal()) {
                        constant = term.getReal();
                    } else if (term.isInteger()) {
                        constant = term.getInteger();
                    }
                }

                try {
                    System.out.println(expr.build().calculate());
                } catch (UnknownFunctionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (UnparsableExpressionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                // find constant in root
                if (math.isReal()) {
                    constant = math.getReal();
                } else if (math.isInteger()) {
                    constant = math.getInteger();
                }
            }

        }

        return constant;
    }

}
