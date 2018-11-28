package ch.ethz.matsim.external_costs.costs.factors.ch;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;

import java.util.HashMap;
import java.util.Map;

public class SwitzerlandEmissionsHealthCostFactors implements EmissionsCostFactors {
    private final String type = "health_costs";

    // costs per ton of CO2-eq
    private final double costPerTonPM10 = 177900;
//
//            57,200
//            29,400

    @Override
    public double getCostFactor(String pollutant) {
        return this.costPerTonPM10;
    }

    @Override
    public String getType() {
        return this.type;
    }
}