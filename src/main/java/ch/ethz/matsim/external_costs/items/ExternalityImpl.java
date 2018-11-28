package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;

public class ExternalityImpl implements Externality {
    public Id<Person> personId;
    public double time;
    public Id<Link> linkId;
    public double value;
}
