package ch.ethz.matsim.external_costs;

import ch.ethz.matsim.external_costs.costs.calculators.EmissionsClimateCostCalculator;
import ch.ethz.matsim.external_costs.costs.calculators.ExternalCostCalculator;
import ch.ethz.matsim.external_costs.costs.factors.ch.SwitzerlandEmissionsClimateCostFactors;
import ch.ethz.matsim.external_costs.items.CumulativeExposureTimeQuadTree;
import ch.ethz.matsim.external_costs.items.ExposureItemFactory;
import ch.ethz.matsim.external_costs.items.ExposureItemFactoryImpl;
import ch.ethz.matsim.external_costs.listeners.ExposureTimeListener;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.Collection;
import java.util.HashSet;

public class Run {
    public static void main(String[] args) {

        String CONFIG_FILE = args[0];
        String EVENT_FILE = args[1];

        // set up config
        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(CONFIG_FILE));

        EventsManager eventsManager = new EventsManagerImpl();

        int numberTimeBins = 30;
        double timeBinLength = 3600.;

        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
        CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree = new CumulativeExposureTimeQuadTree(NetworkUtils.getBoundingBox(scenario.getNetwork().getNodes().values()), numberTimeBins);
        ExposureTimeListener exposureTimeListener = new ExposureTimeListener(scenario, exposureItemFactory, cumulativeExposureTimeQuadTree);

        eventsManager.addHandler(exposureTimeListener);
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
        reader.readFile(EVENT_FILE);

        Collection<ExternalCostCalculator> externalCostCalculators = new HashSet<>();
        externalCostCalculators.add(new EmissionsClimateCostCalculator(new SwitzerlandEmissionsClimateCostFactors()));


        Vehicle2DriverEventHandler v2dh = new Vehicle2DriverEventHandler();

    }
}
