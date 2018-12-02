/* *********************************************************************** *
 * project: org.matsim.*
 * EmissionCostModule.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package ch.ethz.matsim.external_costs.vsp;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;

import java.util.Map;


/**
 * @author benjamin
 *
 */
public class EmissionCostModule {
	private static final Logger logger = Logger.getLogger(EmissionCostModule.class);
	
	private final double emissionCostFactor;
	private boolean considerCO2Costs = false;

	@Inject
	public EmissionCostModule(EmissionsConfigGroup emissionsConfigGroup ) {
		this.emissionCostFactor = emissionsConfigGroup.getEmissionCostMultiplicationFactor();
		this.considerCO2Costs = emissionsConfigGroup.isConsideringCO2Costs();

		logger.info("Emission costs from Maibach et al. (2008) are multiplied by a factor of " + this.emissionCostFactor);
		
		if(considerCO2Costs){
			logger.info("CO2 emission costs will be calculated... ");
		} else {
			logger.info("CO2 emission costs will NOT be calculated... ");
		}
	}

	public double calculateWarmEmissionCosts(Map<String, Double> warmEmissions) {
		double warmEmissionCosts = 0.0;
		
		for(String wp : warmEmissions.keySet()){
			if(wp.equals("CO2(total)") && ! considerCO2Costs) {
				// do nothing
			} else {
				double costFactor = EmissionCostFactors.getCostFactor(wp.toString());
				warmEmissionCosts += warmEmissions.get(wp) * costFactor ;
			}
		}
		return this.emissionCostFactor * warmEmissionCosts;
	}
	
	public double calculateColdEmissionCosts(Map<String, Double> coldEmissions) {
		double coldEmissionCosts = 0.0;
		
		for(String cp : coldEmissions.keySet()){
			double costFactor = EmissionCostFactors.getCostFactor(cp.toString());
			coldEmissionCosts += costFactor * coldEmissions.get(cp);
		}
		return this.emissionCostFactor * coldEmissionCosts;
	}
}
