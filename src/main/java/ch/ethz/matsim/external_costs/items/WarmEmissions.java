package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.emissions.types.WarmPollutant;
import org.matsim.vehicles.Vehicle;

import java.util.Map;

public class WarmEmissions implements Externality {
    private final Id<Person> personId;
    private final Id<Vehicle> vehicleId;
    private final double time;
    private final Id<Link> linkId;
    private final Map<WarmPollutant, Double> warmEmissions;

    public WarmEmissions(Id<Person> personId, Id<Vehicle> vehicleId, double time, Id<Link> linkId, Map<WarmPollutant, Double> warmEmissions) {
        this.personId = personId;
        this.vehicleId = vehicleId;
        this.time = time;
        this.linkId = linkId;
        this.warmEmissions = warmEmissions;
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

    public Map<WarmPollutant, Double> getWarmEmissions() {
        return warmEmissions;
    }
}
