package ch.ethz.matsim.external_costs.costs.factors;

public interface EmissionsCostFactors {
    double getCostFactor(String pollutant);
}
