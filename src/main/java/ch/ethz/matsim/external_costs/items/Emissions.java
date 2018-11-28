package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.vehicles.Vehicle;

import java.util.Map;

public class Emissions implements Externality {
    private final Id<Person> personId;
    private final Id<Vehicle> vehicleId;
    private final double time;
    private final Id<Link> linkId;
    private final Map<String, Double> emissions;

    public Emissions(Id<Person> personId, Id<Vehicle> vehicleId, double time, Id<Link> linkId, Map<String, Double> emissions) {
        this.personId = personId;
        this.vehicleId = vehicleId;
        this.time = time;
        this.linkId = linkId;
        this.emissions = emissions;
    }


    public Id<Person> getPersonId() {
        return personId;
    }

    public Id<Vehicle> getVehicleId() {
        return vehicleId;
    }

    public double getTime() {
        return time;
    }

    public Id<Link> getLinkId() {
        return linkId;
    }

    public Map<String, Double> getEmissions() {
        return emissions;
    }
}
