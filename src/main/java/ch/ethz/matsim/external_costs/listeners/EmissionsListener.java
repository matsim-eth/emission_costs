package ch.ethz.matsim.external_costs.listeners;

import ch.ethz.matsim.external_costs.collectors.EmissionsCollector;
import ch.ethz.matsim.external_costs.items.Emissions;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.emissions.events.ColdEmissionEvent;
import org.matsim.contrib.emissions.events.ColdEmissionEventHandler;
import org.matsim.contrib.emissions.events.WarmEmissionEvent;
import org.matsim.contrib.emissions.events.WarmEmissionEventHandler;
import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;

public class EmissionsListener implements WarmEmissionEventHandler, ColdEmissionEventHandler {
    private Vehicle2DriverEventHandler v2deh;
    private EmissionsCollector emissionsCollector;

    public EmissionsListener(Vehicle2DriverEventHandler v2deh, EmissionsCollector emissionsCollector) {
        this.v2deh = v2deh;
        this.emissionsCollector = emissionsCollector;
    }

    @Override
    public void handleEvent(ColdEmissionEvent event) {
        Id<Person> personId = v2deh.getDriverOfVehicle(event.getVehicleId());
        this.emissionsCollector.addExternality(new Emissions(personId, event.getVehicleId(), event.getTime(), event.getLinkId(), event.getColdEmissions()));
    }

    @Override
    public void handleEvent(WarmEmissionEvent event) {
        Id<Person> personId = v2deh.getDriverOfVehicle(event.getVehicleId());
        this.emissionsCollector.addExternality(new Emissions(personId, event.getVehicleId(), event.getTime(), event.getLinkId(), event.getWarmEmissions()));
    }
}
