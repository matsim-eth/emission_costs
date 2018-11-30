package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.facilities.ActivityFacility;

public class ExposureItem {
    private final Id<Person> personId;
    private final Id<ActivityFacility> facilityId;

    private double activityStartTime;
    private double activityEndTime;


    ExposureItem(Id<Person> personId, Id<ActivityFacility> facilityId) {
        this.personId = personId;
        this.facilityId = facilityId;
    }

    public void setActivityStartTime(double activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public void setActivityEndTime(double activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public double getActivityStartTime() {
        return activityStartTime;
    }

    public double getActivityEndTime() {
        return activityEndTime;
    }

    public double getExposureTime() {
        return this.activityEndTime - this.activityStartTime;
    }

    public Id<Person> getPersonId() {
        return personId;
    }

    public Id<ActivityFacility> getFacilityId() {
        return facilityId;
    }
}
