package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.Random;

import dk.dtu.sb.Util;
import dk.dtu.sb.data.SimulationPoint;
import dk.dtu.sb.spn.Reaction;

/**
 * Concrete implementation of the Algorithm class, specifically Gillespie's
 * algorithm is implemented.
 */
public class GillespieAlgorithm extends Algorithm {

    /**
     * Direct Method (First reaction may be faster)
     */
    public void run() {
    	
        double a_0;
        double tau;
        double deltaTau = 0;
        Reaction R_mu = null;
        int steps = 0;

        Random rand = new Random();

        Util.log.debug("Thread start: " + Thread.currentThread().getId());
        
        while (true) {

            // Step 1
            a_0 = calculate_a0();
            if (a_0 == 0.0) {
                // Nothing more to be done
                break;
            }

            // Step 2
            tau = ((1.0 / a_0) * Math.log(1.0 / rand.nextDouble()));
            R_mu = findReaction(a_0 * rand.nextDouble());

            // Step 3
            time += tau;
            if (time > params.getStoptime()) {
                break;
            }
            Util.updateMarkings(R_mu, currentMarkings);

            // Record time and R_u if >= threshold store result
            deltaTau += tau;
            if(deltaTau >= params.getSimThreshold()){
            	addPartialResult(new SimulationPoint(time, currentMarkings));
            	deltaTau = 0;
            }
            steps ++;
            
            //High concentrations may lead to infinitesimal small tau values = almost infinite steps.
            if(steps % 1000000 == 0){
            	//Only check once in a while
            	if(Thread.currentThread().isInterrupted()){
            		Util.log.debug("Thread : " + Thread.currentThread().getId() +" aborted due to time-out");
            		return;
            		
            	}
            	Util.log.debug("Thread : " + Thread.currentThread().getId() +" at step "+steps);
            }
            
        }
        Util.log.debug("Thread done: " + Thread.currentThread().getId() +" in "+steps+" steps");
    }
    
    /**
     * Returns a reaction that fulfills: \Sum_{v=1}^{mu-1} a_v < val <
     * \Sum_{v=1}^{mu} a_v
     * 
     * @param val
     *            a_0 * random
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
     * a_0 = SUM a_i forall i in SPN. Stores each propensity value in {@link propensities} for reuse.
     * 
     * @return
     */
    protected double calculate_a0() {
        double a_0 = 0;
        // if (R_mu == null) {
        for (Reaction reaction : spn.getReactions().values()) {
            double prop = reaction.calculatePropensity(currentMarkings,params.getRateMode());
        	a_0 += prop;
        	//Store for reuse
        	propensities.put(reaction.getId(), prop);
        }
        /*
         * } else { for (String productId : R_mu.getProducts().keySet()) { for
         * (Reaction reaction : spn.getSpecies(productId).asReactantReactions())
         * { calculatePropensity(reaction); } } for (String reactantId :
         * R_mu.getReactants().keySet()) { for (Reaction reaction :
         * spn.getSpecies(reactantId).asReactantReactions()) {
         * calculatePropensity(reaction); } } for (Reaction reaction :
         * spn.getReactions().values()) { a_0 +=
         * propensities.get(reaction.getId()); } }
         */
        return a_0;
    }
}