package ch.ethz.matsim.external_costs.listeners;

import ch.ethz.matsim.external_costs.items.CumulativeExposureTimeImpl;
import ch.ethz.matsim.external_costs.items.CumulativeExposureTimeQuadTree;
import ch.ethz.matsim.external_costs.items.ExposureItem;
import ch.ethz.matsim.external_costs.items.ExposureItemFactory;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.population.Person;
import org.matsim.facilities.ActivityFacility;

import java.util.HashMap;
import java.util.Map;

public class ExposureTimeListener implements ActivityStartEventHandler, ActivityEndEventHandler {
    private Scenario scenario;
    private ExposureItemFactory exposureItemFactory;
    private Map<Id<Person>, ExposureItem> personId2ExposureItem = new HashMap<>();
    private CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree;

    public ExposureTimeListener(Scenario scenario, ExposureItemFactory exposureItemFactory, CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree) {
        this.scenario = scenario;
        this.exposureItemFactory = exposureItemFactory;
        this.cumulativeExposureTimeQuadTree = cumulativeExposureTimeQuadTree;
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        Id<Person> personId = event.getPersonId();
        ExposureItem exposureItem = this.exposureItemFactory.createExposureItem(personId, event.getFacilityId());
        exposureItem.setActivityStartTime(event.getTime());

        // add item to map
        this.personId2ExposureItem.put(personId, exposureItem);
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        Id<Person> personId = event.getPersonId();
        this.personId2ExposureItem.putIfAbsent(personId, this.exposureItemFactory.createExposureItem(personId, event.getFacilityId()));
        ExposureItem exposureItem = this.personId2ExposureItem.get(personId);
        exposureItem.setActivityEndTime(event.getTime());

        // get coordinate for cumulative exposure times quadtree
        Id<ActivityFacility> facilityId = event.getFacilityId();

        // ignore null facilities for now
        if (facilityId != null) {
            Coord coord = scenario.getActivityFacilities().getFacilities().get(event.getFacilityId()).getCoord();

            // add cumulated exposure times
            this.cumulativeExposureTimeQuadTree.add(new CumulativeExposureTimeImpl(this.exposureItemFactory.getExposurePerTimeBin(exposureItem), coord));
        }

        // remove from map
        this.personId2ExposureItem.remove(personId);
    }

    @Override
    public void reset(int iteration) {
        this.personId2ExposureItem.clear();
    }

}
