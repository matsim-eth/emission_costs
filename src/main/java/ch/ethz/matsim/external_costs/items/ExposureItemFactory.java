package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.facilities.ActivityFacility;

public interface ExposureItemFactory {
    ExposureItem createExposureItem(Id<Person> personId, Id<ActivityFacility> facilityId);
    double[] getExposurePerTimeBin(ExposureItem exposureItem);
    int getTimeBin(double time);
}
