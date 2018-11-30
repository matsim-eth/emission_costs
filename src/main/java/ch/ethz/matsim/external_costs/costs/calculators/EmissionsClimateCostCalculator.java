package ch.ethz.matsim.external_costs.costs.calculators;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;
import ch.ethz.matsim.external_costs.items.Emissions;
import ch.ethz.matsim.external_costs.items.Externality;

public class EmissionsClimateCostCalculator implements ExternalCostCalculator {
    private String type = "emissions_climate_costs";
    private EmissionsCostFactors emissionsCostFactors;

    public EmissionsClimateCostCalculator(EmissionsCostFactors emissionsCostFactors) {
        this.emissionsCostFactors = emissionsCostFactors;
    }

    @Override
    public double calculate(Externality externality) {
        double cost = 0.0;
        if (externality instanceof Emissions) {
            for (String pollutant : ((Emissions) externality).getEmissions().keySet()) {
                cost += ((Emissions) externality).getEmissions().get(pollutant) * this.emissionsCostFactors.getCostFactor(pollutant);
            }
        }
        return cost;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
