package ch.ethz.matsim.external_costs.utils;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Person;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleType;

import java.util.Random;

public class VehicleShareUtils {

    public static void setUpVehicles(Scenario scenario, double shareDiesel) {
        //householdid, #autos, auto1, auto2, auto3
        //get household id of person. Assign next vehicle from household.

        // Petrol car setup
        VehicleType petrol_car = org.matsim.vehicles.VehicleUtils.getFactory().createVehicleType(Id.create(TransportMode.car + "_petrol", VehicleType.class));
        petrol_car.setMaximumVelocity(60.0 / 3.6);
        petrol_car.setPcuEquivalents(1.0);
        petrol_car.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;petrol (4S);>=2L;PC-P-Euro-3END_EMISSIONS");
        scenario.getVehicles().addVehicleType(petrol_car);

        // Diesel car setup
        VehicleType diesel_car = org.matsim.vehicles.VehicleUtils.getFactory().createVehicleType(Id.create(TransportMode.car + "_diesel", VehicleType.class));
        diesel_car.setMaximumVelocity(60.0 / 3.6);
        diesel_car.setPcuEquivalents(1.0);
        diesel_car.setDescription("BEGIN_EMISSIONSPASSENGER_CAR;diesel;<1,4L;PC D Euro-3END_EMISSIONS");
        scenario.getVehicles().addVehicleType(diesel_car);

        // percentage of diesel vehicles
        Random randomGenerator = new Random();

        for (Id<Person> pid : scenario.getPopulation().getPersons().keySet()) {
            Id<Vehicle> vid = Id.createVehicleId(pid);

            //add petrol or diesel vehicles according to percentage
            double share = randomGenerator.nextDouble();
            if (share < shareDiesel) {
                Vehicle v = scenario.getVehicles().getFactory().createVehicle(vid, diesel_car);
                scenario.getVehicles().addVehicle(v);
            } else {
                Vehicle v = scenario.getVehicles().getFactory().createVehicle(vid, petrol_car);
                scenario.getVehicles().addVehicle(v);
            }

            //scenario.getHouseholds().popul  ().get(hid).getVehicleIds().add(vid);
        }
    }


}
