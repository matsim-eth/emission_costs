package ch.ethz.matsim.external_costs.exposure;

import org.matsim.api.core.v01.Coord;

public class ExposureItem {
    private double[] exposureTimes;
    final private Coord coord;

    public ExposureItem(double[] exposureTimes, Coord coord) {
        this.exposureTimes = exposureTimes;
        this.coord = coord;
    }

    public double[] getExposureTimes() {
        return exposureTimes;
    }

    final public Coord getCoord() {
        return coord;
    }
}
