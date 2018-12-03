package ch.ethz.matsim.external_costs.run;

import ch.ethz.matsim.external_costs.utils.VehicleShareUtils;
import ch.ethz.matsim.external_costs.collectors.EmissionCostsCollector;
import ch.ethz.matsim.external_costs.costs.calculators.EmissionHealthCostCalculator;
import ch.ethz.matsim.external_costs.costs.calculators.EmissionsClimateCostCalculator;
import ch.ethz.matsim.external_costs.costs.calculators.ExternalCostCalculator;
import ch.ethz.matsim.external_costs.costs.factors.ch.SwitzerlandEmissionsClimateCostFactors;
import ch.ethz.matsim.external_costs.costs.factors.ch.SwitzerlandEmissionsHealthCostFactors;
import ch.ethz.matsim.external_costs.exposure.ExposureTimeQuadTree;
import ch.ethz.matsim.external_costs.exposure.IntervalHandler;
import ch.ethz.matsim.external_costs.listeners.EmissionsListener;
import ch.ethz.matsim.external_costs.writers.EmissionCostsPerPersonWriter;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.roadTypeMapping.OsmHbefaMapping;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;


public class EmissionsCostsRunner {
    private String configFile;
    private String eventFile;
    private String outputFile;

    public static void main(String[] args) throws IOException {
//
//        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/defaultIVTConfig_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/800.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/emissionsCostPerPerson.csv";

        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/switzerland_config_w_emissions.xml";
        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/20.events.xml.gz";
        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/emissionsCostPerPerson.csv";

//        String configFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/switzerland_config_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/0.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/emissionsHealthCostPerPerson.csv";

//        String configFile = args[0];
//        String eventFile = args[1];
//        String outputFile = args[2];

        new EmissionsCostsRunner(configFile, eventFile, outputFile).run();
    }

    public EmissionsCostsRunner(String configFile, String eventFile, String outputFile) {
        this.configFile = configFile;
        this.eventFile = eventFile;
        this.outputFile = outputFile;
    }

    public void run() throws IOException {
        // set up config
        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup()));

        int timeBinSize = 3600;
        int noOfTimeBins = 30;

        // calculate exposure
        double cellSize = 100.0;
        double exposureDistance = 1000.0;
        double[] boundingBox = NetworkUtils.getBoundingBox(scenario.getNetwork().getNodes().values());
        ExposureTimeQuadTree exposureTimeQuadTree = new ExposureTimeQuadTree(boundingBox, timeBinSize, noOfTimeBins,cellSize);
        IntervalHandler intervalHandler = new IntervalHandler(scenario, exposureTimeQuadTree);

        EventsManager eventsManager = new EventsManagerImpl();
        eventsManager.addHandler(intervalHandler);
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
        reader.readFile(eventFile);

        //add mappings to network
        OsmHbefaMapping.build().addHbefaMappings(scenario.getNetwork());

        // set up vehicles
        VehicleShareUtils.setUpVehicles(scenario, 0.4);

        // handle emissions
        EventsManager eventsManager2 = new EventsManagerImpl();
        Vehicle2DriverEventHandler v2dh = new Vehicle2DriverEventHandler();
        eventsManager2.addHandler(v2dh);

        EmissionModule emissionModule = new EmissionModule(scenario, eventsManager2);

        Collection<ExternalCostCalculator> externalCostCalculators = new HashSet<>();
        externalCostCalculators.add(new EmissionsClimateCostCalculator(new SwitzerlandEmissionsClimateCostFactors()));
        externalCostCalculators.add(new EmissionHealthCostCalculator(scenario, intervalHandler.getExposureTimeQuadTree(), new SwitzerlandEmissionsHealthCostFactors(), exposureDistance));

        EmissionCostsCollector emissionsCollector = new EmissionCostsCollector(externalCostCalculators);
        EmissionsListener emissionsListener = new EmissionsListener(v2dh, emissionsCollector);

        emissionModule.getEmissionEventsManager().addHandler(emissionsListener);
        MatsimEventsReader reader2 = new MatsimEventsReader(eventsManager2);
        reader2.readFile(eventFile);

        new EmissionCostsPerPersonWriter(emissionsCollector.getEmissionsPerPerson()).write(outputFile);

    }
}
