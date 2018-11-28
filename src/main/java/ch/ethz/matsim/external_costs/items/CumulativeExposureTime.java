package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Coord;

public interface CumulativeExposureTime {
    double[] getExposureTimes();
    Coord getCoord();
}
