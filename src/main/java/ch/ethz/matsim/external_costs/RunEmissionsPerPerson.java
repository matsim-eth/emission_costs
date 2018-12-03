package ch.ethz.matsim.external_costs;

import ch.ethz.matsim.external_costs.collectors.EmissionCollector;
import ch.ethz.matsim.external_costs.listeners.EmissionsListener;
import ch.ethz.matsim.external_costs.writers.EmissionsPerPersonWriter;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.roadTypeMapping.OsmHbefaMapping;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.IOException;

public class RunEmissionsPerPerson {
    private String configFile;
    private String eventFile;
    private String outputFile;

    public static void main(String[] args) throws IOException {
//
//        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/defaultIVTConfig_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/800.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/800.emissionsPerPerson.csv";

        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/switzerland_config_w_emissions.xml";
        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/20.events.xml.gz";
        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/emissionsPerPerson.csv";

//        String configFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/switzerland_config_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/0.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/emissionsHealthCostPerPerson.csv";

//        String configFile = args[0];
//        String eventFile = args[1];
//        String outputFile = args[2];

        new RunEmissionsPerPerson(configFile, eventFile, outputFile).run();
    }

    public RunEmissionsPerPerson(String configFile, String eventFile, String outputFile) {
        this.configFile = configFile;
        this.eventFile = eventFile;
        this.outputFile = outputFile;
    }

    public void run() throws IOException {
        // set up config
        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup()));

        //add mappings to network
        OsmHbefaMapping.build().addHbefaMappings(scenario.getNetwork());

        // set up vehicles
        VehicleShareUtils.setUpVehicles(scenario, 0.4);

        // handle emissions
        EventsManager eventsManager = new EventsManagerImpl();
        Vehicle2DriverEventHandler v2dh = new Vehicle2DriverEventHandler();
        eventsManager.addHandler(v2dh);

        EmissionModule emissionModule = new EmissionModule(scenario, eventsManager);
        EmissionCollector emissionCollector = new EmissionCollector();
        EmissionsListener emissionsListener = new EmissionsListener(v2dh, emissionCollector);

        emissionModule.getEmissionEventsManager().addHandler(emissionsListener);
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
        reader.readFile(eventFile);

        new EmissionsPerPersonWriter(emissionCollector.getEmissionsPerPerson()).write(outputFile);
    }
}
