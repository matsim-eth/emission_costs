package ch.ethz.matsim.external_costs.collectors;

import ch.ethz.matsim.external_costs.items.Externality;

public interface ExternalityCollector {
    void addExternality(Externality externality);
}
