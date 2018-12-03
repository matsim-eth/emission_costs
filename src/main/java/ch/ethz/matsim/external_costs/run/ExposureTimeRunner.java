package ch.ethz.matsim.external_costs.run;

import ch.ethz.matsim.external_costs.exposure.ExposureTimeQuadTree;
import ch.ethz.matsim.external_costs.exposure.IntervalHandler;
import ch.ethz.matsim.external_costs.writers.CSVExposureTimeWriter;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.IOException;

public class ExposureTimeRunner {
    private String configFile;
    private String eventFile;
    private String outputFile;

    public static void main(String[] args) throws IOException {
//
        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/defaultIVTConfig_w_emissions.xml";
        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/800.events.xml.gz";
        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/exposureTimes.csv";

//        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/switzerland_config_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/20.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/emissionsCostPerPerson.csv";

//        String configFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/switzerland_config_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/0.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/emissionsHealthCostPerPerson.csv";

//        String configFile = args[0];
//        String eventFile = args[1];
//        String outputFile = args[2];

        new ExposureTimeRunner(configFile, eventFile, outputFile).run();
    }

    public ExposureTimeRunner(String configFile, String eventFile, String outputFile) {
        this.configFile = configFile;
        this.eventFile = eventFile;
        this.outputFile = outputFile;
    }

    public void run() throws IOException {
        // set up config
        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup()));

        int timeBinSize = 3600;
        int noOfTimeBins = 30;
        double cellSize = 100.0;

        // calculate exposure
        double[] boundingBox = NetworkUtils.getBoundingBox(scenario.getNetwork().getNodes().values());
        ExposureTimeQuadTree exposureTimeQuadTree = new ExposureTimeQuadTree(boundingBox, timeBinSize, noOfTimeBins,cellSize);
        IntervalHandler intervalHandler = new IntervalHandler(scenario, exposureTimeQuadTree);

        EventsManager eventsManager = new EventsManagerImpl();
        eventsManager.addHandler(intervalHandler);
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
        reader.readFile(eventFile);

        new CSVExposureTimeWriter(intervalHandler.getExposureTimeQuadTree()).write(outputFile);

    }
}
