package ch.ethz.matsim.external_costs.collectors;

import ch.ethz.matsim.external_costs.costs.calculators.ExternalCostCalculator;
import ch.ethz.matsim.external_costs.items.Emissions;
import ch.ethz.matsim.external_costs.items.Externality;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EmissionsCollector implements ExternalityCollector {
    private Collection<ExternalCostCalculator> externalCostCalculators;
    private Map<Id<Person>, Map<String, Double>> personId2CostType2Value = new HashMap<>();

    public EmissionsCollector(Collection<ExternalCostCalculator> externalCostCalculators) {
        this.externalCostCalculators = externalCostCalculators;
    }

    @Override
    public void addExternality(Externality externality) {
        if (externality instanceof Emissions) {

            Id<Person> personId = ((Emissions) externality).getPersonId();
            personId2CostType2Value.putIfAbsent(personId, new HashMap<>());

            for (ExternalCostCalculator externalCostCalculator : externalCostCalculators) {

                personId2CostType2Value.get(personId).putIfAbsent(externalCostCalculator.getType(), 0.0);

                double costs = personId2CostType2Value.get(personId).get(externalCostCalculator.getType()) + externalCostCalculator.calculate(externality);
                personId2CostType2Value.get(personId).put(externalCostCalculator.getType(), costs);
            }
        }
    }
}
