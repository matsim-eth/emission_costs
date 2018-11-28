package ch.ethz.matsim.external_costs.listeners;

import ch.ethz.matsim.external_costs.collectors.EmissionsCollector;
import ch.ethz.matsim.external_costs.items.Emissions;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.emissions.events.ColdEmissionEvent;
import org.matsim.contrib.emissions.events.ColdEmissionEventHandler;
import org.matsim.contrib.emissions.events.WarmEmissionEvent;
import org.matsim.contrib.emissions.events.WarmEmissionEventHandler;
import org.matsim.contrib.emissions.types.ColdPollutant;
import org.matsim.contrib.emissions.types.WarmPollutant;
import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;
import org.matsim.vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class EmissionsListener implements WarmEmissionEventHandler, ColdEmissionEventHandler {
    private Vehicle2DriverEventHandler v2deh;
    private EmissionsCollector emissionsCollector;

    public EmissionsListener(Vehicle2DriverEventHandler v2deh, EmissionsCollector emissionsCollector) {
        this.v2deh = v2deh;
        this.emissionsCollector = emissionsCollector;
    }

    @Override
    public void handleEvent(ColdEmissionEvent event) {
        Id<Vehicle> vehicleId = event.getVehicleId();
        Id<Person> personId = v2deh.getDriverOfVehicle(vehicleId);

        Map<String, Double> emissions = new HashMap<>();
        for (ColdPollutant coldPollutant : event.getColdEmissions().keySet()) {
            emissions.put(coldPollutant.getText(), event.getColdEmissions().get(coldPollutant));
        }
        this.emissionsCollector.addExternality(new Emissions(personId, vehicleId, event.getTime(), event.getLinkId(), emissions));
    }

    @Override
    public void handleEvent(WarmEmissionEvent event) {
        Id<Vehicle> vehicleId = event.getVehicleId();
        Id<Person> personId = v2deh.getDriverOfVehicle(vehicleId);

        Map<String, Double> emissions = new HashMap<>();
        for (WarmPollutant warmPollutant : event.getWarmEmissions().keySet()) {
            emissions.put(warmPollutant.getText(), event.getWarmEmissions().get(warmPollutant));
        }
        this.emissionsCollector.addExternality(new Emissions(personId, vehicleId, event.getTime(), event.getLinkId(), emissions));
    }
}
