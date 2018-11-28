package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.facilities.ActivityFacility;

public class ExposureItemFactoryImpl implements ExposureItemFactory {
    private final int numberTimeBins;
    private final double timeBinLength;

    public ExposureItemFactoryImpl(int numberTimeBins, double timeBinLength) {
        this.numberTimeBins = numberTimeBins;
        this.timeBinLength = timeBinLength;
    }

    @Override
    public ExposureItem createExposureItem(Id<Person> personId, Id<ActivityFacility> facilityId) {
        ExposureItem exposureItem = new ExposureItem(personId, facilityId);
        exposureItem.setActivityStartTime(0.0);
        exposureItem.setActivityEndTime(this.numberTimeBins * this.timeBinLength);
        return exposureItem;
    }

    @Override
    public double[] getExposurePerTimeBin(ExposureItem exposureItem) {
        int startBin = getTimeBin(exposureItem.getActivityStartTime());
        int endBin = getTimeBin(exposureItem.getActivityEndTime());

        double[] exposureTimes = new double[numberTimeBins];

        exposureTimes[startBin] = (startBin + 1) * this.timeBinLength - exposureItem.getActivityStartTime();
        for (int i = startBin+1; i<endBin; i++) {
            exposureTimes[i] = this.timeBinLength;
        }
        exposureTimes[endBin] = exposureItem.getActivityEndTime() - endBin * this.timeBinLength;

        return exposureTimes;
    }

    @Override
    public int getTimeBin(double time) {
        int timeBin = (int) Math.floor(time / this.timeBinLength);
        if (timeBin > (this.numberTimeBins - 1)) {
            timeBin = (this.numberTimeBins - 1);
        }
        return timeBin;
    }
}
