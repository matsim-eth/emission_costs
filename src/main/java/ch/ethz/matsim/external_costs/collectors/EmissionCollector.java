package ch.ethz.matsim.external_costs.collectors;

import ch.ethz.matsim.external_costs.items.Emissions;
import ch.ethz.matsim.external_costs.items.Externality;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import java.util.HashMap;
import java.util.Map;

public class EmissionCollector implements ExternalityCollector {
    private Map<Id<Person>, Map<String, Double>> emissionsPerPerson = new HashMap<>();

    @Override
    public void addExternality(Externality externality) {
        if (externality instanceof Emissions) {
            Id<Person> personId = ((Emissions) externality).getPersonId();
            emissionsPerPerson.putIfAbsent(personId, new HashMap<>());

            for (Map.Entry<String, Double> entry : ((Emissions) externality).getEmissions().entrySet()) {
                String pollutant = entry.getKey();
                double value = entry.getValue();

                emissionsPerPerson.get(personId).putIfAbsent(pollutant, 0.0);
                double total = emissionsPerPerson.get(personId).get(pollutant) + value;
                emissionsPerPerson.get(personId).put(pollutant, total);
            }

        }
    }

    public Map<Id<Person>, Map<String, Double>> getEmissionsPerPerson() {
        return emissionsPerPerson;
    }
}
