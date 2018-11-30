package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

import java.util.Map;

public class EmisisonsPerPersonItem implements PerPersonItem{
    private Id<Person> personId;
    private Map<String, Double> attributes;

    public EmisisonsPerPersonItem(Id<Person> personId, Map<String, Double> attributes) {
        this.personId = personId;
        this.attributes = attributes;
    }

    @Override
    public Id<Person> getPersonId() {
        return null;
    }

    @Override
    public Map<String, Double> getAttributes() {
        return null;
    }
}
