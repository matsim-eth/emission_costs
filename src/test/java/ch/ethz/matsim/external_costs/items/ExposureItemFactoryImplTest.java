package ch.ethz.matsim.external_costs.items;

import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.core.population.PopulationUtils;
import org.matsim.facilities.ActivityFacility;

import java.util.Random;

import static org.junit.Assert.*;

public class ExposureItemFactoryImplTest {

    @Test
    public void createExposureItem() {
        int numberTimeBins = 30;
        double timeBinLength = 3600.;
        Id<Person> personId = Id.createPersonId("person");
        Id<ActivityFacility> facilityId = Id.create("facility", ActivityFacility.class);

        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
        ExposureItem exposureItem = exposureItemFactory.createExposureItem(personId, facilityId);

        assertEquals(0.0, exposureItem.getActivityStartTime(), 0.0);
        assertEquals(numberTimeBins*timeBinLength, exposureItem.getActivityEndTime(), 0.0);
    }

    @Test
    public void setExposureItemStartTime() {
        int numberTimeBins = 30;
        double timeBinLength = 3600.;
        Id<Person> personId = Id.createPersonId("person");
        Id<ActivityFacility> facilityId = Id.create("facility", ActivityFacility.class);

        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
        ExposureItem exposureItem = exposureItemFactory.createExposureItem(personId, facilityId);

        Random random = new Random(0);

        for (int i = 0; i<1000; i++) {
            double startTime = random.nextDouble() * numberTimeBins * timeBinLength;
            exposureItem.setActivityStartTime(startTime);

            assertEquals(startTime, exposureItem.getActivityStartTime(), 0.0);
            assertEquals(numberTimeBins*timeBinLength, exposureItem.getActivityEndTime(), 0.0);
        }

    }

    @Test
    public void setExposureItemEndTime() {
        int numberTimeBins = 30;
        double timeBinLength = 3600.;
        Id<Person> personId = Id.createPersonId("person");
        Id<ActivityFacility> facilityId = Id.create("facility", ActivityFacility.class);

        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
        ExposureItem exposureItem = exposureItemFactory.createExposureItem(personId, facilityId);

        Random random = new Random(0);

        for (int i = 0; i<1000; i++) {
            double endTime = random.nextDouble() * numberTimeBins * timeBinLength;
            exposureItem.setActivityEndTime(endTime);

            assertEquals(0.0, exposureItem.getActivityStartTime(), 0.0);
            assertEquals(endTime, exposureItem.getActivityEndTime(), 0.0);
        }

    }

    @Test
    public void getExposurePerTimeBin() {
        int numberTimeBins = 30;
        double timeBinLength = 3600.;
        Id<Person> personId = Id.createPersonId("person");
        Id<ActivityFacility> facilityId = Id.create("facility", ActivityFacility.class);

        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);
        ExposureItem exposureItem = exposureItemFactory.createExposureItem(personId, facilityId);

        exposureItem.setActivityStartTime(5600.0);
        exposureItem.setActivityEndTime(18600.0);

        double[] expectedExposurePerTimeBin = new double[numberTimeBins];
        expectedExposurePerTimeBin[1] = 1600.0;
        expectedExposurePerTimeBin[2] = 3600.0;
        expectedExposurePerTimeBin[3] = 3600.0;
        expectedExposurePerTimeBin[4] = 3600.0;
        expectedExposurePerTimeBin[5] =  600.0;

        double[] actualExposurePerTimeBin = exposureItemFactory.getExposurePerTimeBin(exposureItem);

        for (int i = 0; i<numberTimeBins; i++) {
            assertEquals(expectedExposurePerTimeBin[i], actualExposurePerTimeBin[i], 0.0);
        }

    }

    @Test
    public void getTimeBin() {
        int numberTimeBins = 30;
        double timeBinLength = 3600.;
        ExposureItemFactory exposureItemFactory = new ExposureItemFactoryImpl(numberTimeBins, timeBinLength);

        assertEquals(0, exposureItemFactory.getTimeBin(100.0));
        assertEquals(5, exposureItemFactory.getTimeBin(18970.0));
        assertEquals(12, exposureItemFactory.getTimeBin(44660.0));
        assertEquals(20, exposureItemFactory.getTimeBin(72000.0));
        assertEquals(27, exposureItemFactory.getTimeBin(98500.0));
        assertEquals(29, exposureItemFactory.getTimeBin(108000.0));

    }
}