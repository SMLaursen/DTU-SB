package dk.dtu.sb.parser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.ExplicitRule;
import org.sbml.jsbml.FunctionDefinition;
import org.sbml.jsbml.InitialAssignment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.RateFunction;
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
                newReaction.addReactant(s.getId());
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

        //double rate = getConstant(reaction.getKineticLaw().getMath(), 1.0);
        
        RateFunction rateFunction = getRateFunction(reaction.getKineticLaw());

        return new Reaction(reaction.getId(), reaction.getName(), rateFunction);
    }

    /**
     * Sets the initial markings of the SPN.
     */
    private void parseSpecies() {
        for (Species specie : model.getListOfSpecies()) {
            spn.addSpecies(new dk.dtu.sb.data.Species(specie.getId(), specie.getName()));
            spn.setInitialMarking(specie.getId(), (int) specie.getInitialAmount());
        }

        for (InitialAssignment ia : model.getListOfInitialAssignments()) {
            int marking = (int) getConstant(ia.getMath(),
                    spn.getInitialMarking(ia.getVariable()));

            spn.setInitialMarking(ia.getVariable(), marking);
        }
    }
    
    private List<String> unknowns = new ArrayList<String>();
    
    private RateFunction getRateFunction(KineticLaw kl) {
        RateFunction rateFunction = new RateFunction(1.0);
        
        if (kl != null && kl.getMath() != null) {
            unknowns = new ArrayList<String>();
            ASTNode math = replaceVars(kl, kl.getMath());
            rateFunction = new RateFunction(math, unknowns);
        }
        
        return rateFunction;
    }
    
    public ASTNode replaceVars(KineticLaw kl, ASTNode root) {   
        if (!root.isNumber()) {
            for (Enumeration<TreeNode> it = root.children(); it.hasMoreElements(); ) {
                ASTNode term = (ASTNode)it.nextElement();
                int index = root.getIndex(term);
                ASTNode replace = new ASTNode(1);
                
                if (term.isName()) {
                    // local
                    LocalParameter local = kl.getLocalParameter(term.getName());
                    // global
                    Parameter param = model.getParameter(term.getName());
                    if (local != null) {
                        if (local.isSetValue()) {
                            replace = new ASTNode(local.getValue());
                        }
                    } else if (param != null) {
                        if (param.isSetValue()) {
                            replace = new ASTNode(param.getValue());                        
                        } else {
                            // rule
                            ExplicitRule rule = model.getRule(param.getId());
                            if (rule != null) {
                                replace = replaceVars(kl, rule.getMath());
                            }
                        }
                    // modifier, product or reactant
                    } else {
                        replace = term;
                        unknowns.add(term.getName());
                    }
                } /*else if (term.isFunction() && term.getName() != null) {
                    FunctionDefinition function = model.getFunctionDefinition(term.getName());
                    if (function != null) {
                        System.out.println("fn " +function);
                        replace = replaceVars(kl, function.getMath());
                    }
                }*/ else {
                    replace = replaceVars(kl, term);
                }
                
                root.replaceChild(index, replace);
            }
        }
        
        return root;
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
                // find constant in expression
                for (ASTNode term : math.getChildren()) {
                    if (term.isReal()) {
                        constant = term.getReal();
                    } else if (term.isInteger()) {
                        constant = term.getInteger();
                    }
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
