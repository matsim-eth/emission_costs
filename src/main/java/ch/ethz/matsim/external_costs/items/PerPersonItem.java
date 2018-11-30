package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import java.util.Map;

public interface PerPersonItem {
    Id<Person> getPersonId();
    Map<String, Double> getAttributes();
}
