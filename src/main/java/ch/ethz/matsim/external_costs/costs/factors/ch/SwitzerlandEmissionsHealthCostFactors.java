package ch.ethz.matsim.external_costs.costs.factors.ch;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;

public class SwitzerlandEmissionsHealthCostFactors implements EmissionsCostFactors {
    // costs per g of PM10
    private final double costPerTonPM10 = 1.0;
//             177900 / 1000. / 1000.
//            57,200
//            29,400

    @Override
    public double getCostFactor(String pollutant) {
        if (pollutant.equals("PM")) {
            return this.costPerTonPM10;
        }
        return 0.0;
    }
}