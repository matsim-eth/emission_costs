package ch.ethz.matsim.external_costs.costs.factors.ch;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;

import java.util.HashMap;
import java.util.Map;

public class SwitzerlandEmissionsClimateCostFactors implements EmissionsCostFactors {
    // costs per g of CO2-eq
    private final double costPerTonCO2eq = 121.5 / 1000. / 1000.;
    private final Map<String, Integer> globalWarmingPotential = new HashMap<>();

    public SwitzerlandEmissionsClimateCostFactors() {
        globalWarmingPotential.putIfAbsent("CO2(total)",   1);
        globalWarmingPotential.putIfAbsent("CH4"       ,  23);
        globalWarmingPotential.putIfAbsent("N2O"       , 296);
    }

    @Override
    public double getCostFactor(String pollutant) {
        if (globalWarmingPotential.keySet().contains(pollutant)) {
            return this.costPerTonCO2eq * globalWarmingPotential.get(pollutant);
        }
        return 0.0;
    }
}
