package ch.ethz.matsim.external_costs.costs.factors.ch;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;

import java.util.HashMap;
import java.util.Map;

public class SwitzerlandEmissionsClimateCostFactors implements EmissionsCostFactors {
    private final String type = "climate_costs";

    // costs per ton of CO2-eq
    private final double costPerTonCO2eq = 121.5;
    private final Map<String, Integer> globalWarmingPotential = new HashMap<>();

    public SwitzerlandEmissionsClimateCostFactors() {
        globalWarmingPotential.putIfAbsent("CO2(total)",   1);
        globalWarmingPotential.putIfAbsent("CH4"       ,  23);
        globalWarmingPotential.putIfAbsent("N2O"       , 296);
    }

    @Override
    public double getCostFactor(String pollutant) {
        return this.costPerTonCO2eq * globalWarmingPotential.get(pollutant);
    }

    @Override
    public String getType() {
        return this.type;
    }
}
