package ch.ethz.matsim.external_costs.costs.calculators;

import ch.ethz.matsim.external_costs.items.Externality;

public interface ExternalCostCalculator {
    double calculate(Externality externality);
    String getType();
}
