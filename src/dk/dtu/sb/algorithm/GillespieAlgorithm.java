package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dk.dtu.sb.Util;
import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.ReactionEvent;
import dk.dtu.sb.data.Species;

/**
 * Concrete implementation of the Algorithm class, specifically Gillespie's 
 * algorithm is implemented.
 */
public class GillespieAlgorithm extends Algorithm {

    /**
     * 
     */
    private Map<String, Double> propensities = new HashMap<String, Double>();

    // Direct Method (First reaction may be faster)
    public void run() {

        // Initialize
        double time = 0.0;

        double a_0;
        double r_1;
        double r_2;
        double tau;
        Reaction R_mu;

        Random rand = new Random();

        while (true) {

            // Step 1
            a_0 = calculate_a0();
            if (a_0 == 0.0) {
                // Nothing more to be done
                break;
            }

            // Step 2
            r_1 = rand.nextDouble();
            r_2 = rand.nextDouble();
            tau = ((1.0 / a_0) * Math.log(1.0 / r_1));
            Util.log.debug("a_0 : " + a_0 + "    r_1 :" + r_1 + "     tau :"
                    + tau);
            R_mu = findReaction(a_0 * r_2);

            // Step 3
            time += tau;
            if (time > stoptime) {
                break;
            }
            updateMarkings(R_mu, currentMarkings);

            // Record time and R_u
            addPartialResult(new ReactionEvent(time, R_mu));
            Util.log.debug(time + " :" + currentMarkings);
        }
    }

    // TODO determine if these should go here. They are general but Gillespie
    // specific!

    /**
     * Returns a reaction that fulfills: \Sum_{v=1}^{mu-1} a_v < val <
     * \Sum_{v=1}^{mu} a_v
     * 
     * @param val a_0 * random
     * @return {@link Reaction}.
     */
    private Reaction findReaction(double val) {
        double sum = 0;
        for (Reaction reaction : spn.getReactions().values()) {
            sum += propensities.get(reaction.getId());
            if (sum >= val) {
                return reaction;
            }
        }
        throw new RuntimeException("Failed to find the reaction for a = " + val);
    }

    /**
     * a_0 = SUM a_i forall i in SPN
     * 
     * @return
     */
    private double calculate_a0() {
        double a_0 = 0;
        for (Reaction reaction : spn.getReactions().values()) {
            a_0 += calculatePropensity(reaction);
        }
        return a_0;
    }

    /**
     * Returns the propensity a_r = rate * PROD binom(m_i,f_i) forall i in
     * Reaction reaction
     * 
     * @param reaction
     * @return
     */
    private double calculatePropensity(Reaction reaction) {
        double h = 1.0;
        for (Species reactant : reaction.getReactants().values()) {
            int marking = currentMarkings.get(reactant.getId());
            h *= Util.binom(marking, reactant.getMultiplicity());
        }
        h *= reaction.getRate();
        // Set propensity to avoid recalculations later
        propensities.put(reaction.getId(), h);
        return h;
    }
    
}
