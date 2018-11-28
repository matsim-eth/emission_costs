package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Coord;

public class CumulativeExposureTimeImpl implements CumulativeExposureTime {
    private double[] exposureTimes;
    private Coord coord;

    public CumulativeExposureTimeImpl(double[] exposureTimes, Coord coord) {
        this.exposureTimes = exposureTimes;
        this.coord = coord;
    }

    @Override
    public double[] getExposureTimes() {
        return exposureTimes;
    }

    @Override
    public Coord getCoord() {
        return coord;
    }
}
