package dk.dtu.sb.algorithm;

import java.util.HashMap;
import java.util.Random;

import dk.dtu.sb.Util;
import dk.dtu.sb.data.Reaction;
import dk.dtu.sb.data.ReactionEvent;

public class GillespieAlgorithm extends Algorithm {
	private HashMap<String,Integer> currentMarkings;
	
	//Direct Method (First reaction may be faster)
	public void run(double stoptime){
		
		//Initialize
		double time = 0.0;
		int step = 0;
		
		double a_0;
		double r_1;
		double r_2;
		double tau;
		Reaction R_mu;
		
		Random rand = new Random();
		
		currentMarkings = new HashMap<String,Integer>();
		currentMarkings.putAll(spn.getInitialMarkings());
	
		while(time < stoptime){
			//Step 1
			a_0 = calculate_a0();
			if(a_0 == 0.0){
				//Nothing more to be done
				break;
			}
			
			//Step 2
			r_1 = rand.nextDouble();
			r_2 = rand.nextDouble();
			tau = ((1.0 / a_0)*Math.log(1.0 /r_1));
			Util.log.info("a_0 : "+a_0+"    r_1 :"+r_1+"     tau :"+tau);
			R_mu = findReaction(a_0*r_2); 
					
			//Step 3
			time += tau;
			Util.updateMarkings(R_mu, currentMarkings);
			step++;
			
			//Record time and R_u
			resultData.add(new ReactionEvent(time,R_mu));
			Util.log.info(time +" :" +currentMarkings);
		}
	}
	
	//TODO determine if these should go here. They are general but Gillespie specific!	
	
	/**Returns a reaction that fulfills:
	 * \Sum_{v=1}^{mu-1} a_v < val < \Sum_{v=1}^{mu} a_v*/
	private Reaction findReaction(double val){
		double sum = 0;
		for(Reaction r : spn.getReactions().values()){
			sum += r.getPropensity();
			if(sum >= val){
				return r;
			}
		}
		throw new RuntimeException("Failed to find the reaction for a = "+ val);
	}	
	
	/** a_0 = SUM a_i forall i in SPN */
	private double calculate_a0(){
		double a_0 = 0;
		for(Reaction r : spn.getReactions().values()){
			a_0 += calculatePropensity(r);
		}
		return a_0;
	}
	
	/** Returns the propensity a_r = rate * PROD binom(m_i,f_i) forall i in Reaction r*/
	private double calculatePropensity(Reaction r){
		int h = 1;
		for(String s : r.getReactants().keySet()){
			int marking = currentMarkings.get(s);
			int multiplicity = r.getReactants().get(s);
			h *= binom(marking,multiplicity);
		}
		h *= r.getRate();
		//Set propensity to avoid recalculations later
		r.setPropensity(h);
		return h;
	}
	
	/** Returns the number of combinations for (n k) */
	private int binom(int n, int k){
		//Avoid factorial calculations
		long coeff = 1;
		for (int i = n - k + 1; i <= n; i++) {
			coeff *= i;
		}
		for (int i = 1; i <= k; i++) {
			coeff /= i;
		}
		return (int) coeff;
	}
}
