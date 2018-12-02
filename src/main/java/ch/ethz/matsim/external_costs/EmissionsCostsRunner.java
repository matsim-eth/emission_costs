package ch.ethz.matsim.external_costs;

//import ch.ethz.matsim.external_costs.collectors.EmissionsCollector;
//import ch.ethz.matsim.external_costs.costs.calculators.EmissionHealthCostCalculator;
//import ch.ethz.matsim.external_costs.costs.calculators.EmissionsClimateCostCalculator;
//import ch.ethz.matsim.external_costs.costs.calculators.ExternalCostCalculator;
//import ch.ethz.matsim.external_costs.costs.factors.ch.SwitzerlandEmissionsClimateCostFactors;
//import ch.ethz.matsim.external_costs.costs.factors.ch.SwitzerlandEmissionsHealthCostFactors;
//import ch.ethz.matsim.external_costs.dispersion.DispersionModel;
//import ch.ethz.matsim.external_costs.dispersion.MeanGaussianDispersionModelOverSquareArea;
//import ch.ethz.matsim.external_costs.items.CumulativeExposureTimeQuadTree;
//import ch.ethz.matsim.external_costs.items.ExposureItemFactory;
//import ch.ethz.matsim.external_costs.items.ExposureItemFactoryImpl;
//import ch.ethz.matsim.external_costs.listeners.EmissionsListener;
//import ch.ethz.matsim.external_costs.listeners.ExposureTimeListener;
//import ch.ethz.matsim.external_costs.writers.PerPersonWriter;
//import org.matsim.api.core.v01.Id;
//import org.matsim.api.core.v01.Scenario;
//import org.matsim.api.core.v01.TransportMode;
//import org.matsim.api.core.v01.population.Person;
//import org.matsim.contrib.emissions.EmissionModule;
//import org.matsim.contrib.emissions.roadTypeMapping.OsmHbefaMapping;
//import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
//import org.matsim.core.api.experimental.events.EventsManager;
//import org.matsim.core.config.ConfigUtils;
//import org.matsim.core.events.EventsManagerImpl;
//import org.matsim.core.events.MatsimEventsReader;
//import org.matsim.core.events.algorithms.Vehicle2DriverEventHandler;
//import org.matsim.core.network.NetworkUtils;
//import org.matsim.core.scenario.ScenarioUtils;
//import org.matsim.vehicles.Vehicle;
//import org.matsim.vehicles.VehicleType;
//import org.matsim.vehicles.VehicleUtils;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Random;
//
//public class EmissionsCostsRunner {
//    private String configFile;
//    private String eventFile;
//    private String outputFile;
//
//    public static void main(String[] args) throws IOException {
//
////        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/defaultIVTConfig_w_emissions.xml";
////        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/800.events.xml.gz";
////        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/zurich_1pct/scenario/emissionsPerPerson.csv";
//
////        String configFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/switzerland_config_w_emissions.xml";
////        String eventFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/20.events.xml.gz";
////        String outputFile = "/home/ctchervenkov/Documents/projects/road_pricing/switzerland_10pct/emissionsPerPerson.csv";
//
//        String configFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/switzerland_config_w_emissions.xml";
//        String eventFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/0.events.xml.gz";
//        String outputFile = "/home/ctchervenkov/Documents/scenarios/pipeline/tchervi10pct/emissionsPerPerson.csv";
//
////        String configFile = args[0];
////        String eventFile = args[1];
////        String outputFile = args[2];
//        new EmissionsCostsRunner(configFile, eventFile, outputFile).run();
//    }
//
//    public EmissionsCostsRunner(String configFile, String eventFile, String outputFile) {
//        this.configFile = configFile;
//        this.eventFile = eventFile;
//        this.outputFile = outputFile;
//    }
//
//    public void run() throws IOException {
//        // set up config
//        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup()));
//
//        //add mappings to network
//        OsmHbefaMapping.build().addHbefaMappings(scenario.getNetwork());
//
//        setUpVehicles(scenario);
//
//        int numberTimeBins = 30;
//        double timeBinLength = 3600.;
//
//        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
//        CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree = new CumulativeExposureTimeQuadTree(NetworkUtils.getBoundingBox(scenario.getNetwork().getNodes().values()), numberTimeBins);
//        ExposureTimeListener exposureTimeListener = new ExposureTimeListener(scenario, exposureItemFactory, cumulativeExposureTimeQuadTree);
//
//        EventsManager eventsManager1 = new EventsManagerImpl();
//        eventsManager1.addHandler(exposureTimeListener);
//        MatsimEventsReader reader1 = new MatsimEventsReader(eventsManager1);
//        reader1.readFile(eventFile);
//        exposureTimeListener.finish();
//
//        Collection<ExternalCostCalculator> externalCostCalculators = new HashSet<>();
//        externalCostCalculators.add(new EmissionsClimateCostCalculator(new SwitzerlandEmissionsClimateCostFactors()));
//
//        DispersionModel dispersionModel = new MeanGaussianDispersionModelOverSquareArea(250.0, 100.0);
//        externalCostCalculators.add(new EmissionHealthCostCalculator(scenario, exposureItemFactory, new SwitzerlandEmissionsHealthCostFactors(), cumulativeExposureTimeQuadTree, dispersionModel, 500.0));
//
//        // handle emissions
//        EventsManager eventsManager2 = new EventsManagerImpl();
//        Vehicle2DriverEventHandler v2dh = new Vehicle2DriverEventHandler();
//        eventsManager2.addHandler(v2dh);
//
//        EmissionModule emissionModule = new EmissionModule(scenario, eventsManager2);
//        EmissionsCollector emissionsCollector = new EmissionsCollector(externalCostCalculators);
//        EmissionsListener emissionsListener = new EmissionsListener(v2dh, emissionsCollector);
//
//        emissionModule.getEmissionEventsManager().addHandler(emissionsListener);
//        MatsimEventsReader reader2 = new MatsimEventsReader(eventsManager2);
//        reader2.readFile(eventFile);
//
//        new PerPersonWriter(emissionsCollector.getEmissionsPerPerson()).write(outputFile);
//
//    }
//
//    private void setUpVehicles(Scenario scenario) {
//        //householdid, #autos, auto1, auto2, auto3
//        //get household id of person. Assign next vehicle from household.
//
//        // Petrol car setup
//        VehicleType petrol_car = VehicleUtils.getFactory().createVehicleType(Id.create(TransportMode.car + "_petrol", VehicleType.class));
//        petrol_car.setMaximumVelocity(60.0 / 3.6);
//        petrol_car.setPcuEquivalents(1.0);
//        petrol_car.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;petrol (4S);>=2L;PC-P-Euro-3END_EMISSIONS");
//        scenario.getVehicles().addVehicleType(petrol_car);
//
//        // Diesel car setup
//        VehicleType diesel_car = VehicleUtils.getFactory().createVehicleType(Id.create(TransportMode.car + "_diesel", VehicleType.class));
//        diesel_car.setMaximumVelocity(60.0 / 3.6);
//        diesel_car.setPcuEquivalents(1.0);
//        diesel_car.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;diesel;<1,4L;PC D Euro-3END_EMISSIONS");
//        scenario.getVehicles().addVehicleType(diesel_car);
//
//        // percentage of diesel vehicles
//        Random randomGenerator = new Random();
//        double percentDiesel = 0.4;
//
//        for (Id<Person> pid : scenario.getPopulation().getPersons().keySet()) {
//            Id<Vehicle> vid = Id.createVehicleId(pid);
//
//            //add petrol or diesel vehicles according to percentage
//            double percent = randomGenerator.nextDouble();
//            if (percent < percentDiesel) {
//                Vehicle v = scenario.getVehicles().getFactory().createVehicle(vid, diesel_car);
//                scenario.getVehicles().addVehicle(v);
//            } else {
//                Vehicle v = scenario.getVehicles().getFactory().createVehicle(vid, petrol_car);
//                scenario.getVehicles().addVehicle(v);
//            }
//
//            //scenario.getHouseholds().popul  ().get(hid).getVehicleIds().add(vid);
//        }
//
//    }
//}
