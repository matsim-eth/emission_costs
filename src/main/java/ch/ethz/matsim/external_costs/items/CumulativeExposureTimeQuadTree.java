package ch.ethz.matsim.external_costs.items;

import org.matsim.api.core.v01.Coord;
import org.matsim.core.utils.collections.QuadTree;

import java.util.Collection;

public class CumulativeExposureTimeQuadTree {
    private QuadTree<CumulativeExposureTime> quadTree;
    private int numberTimeBins;

    public CumulativeExposureTimeQuadTree(double[] boundingBox, int numberTimeBins) {
        this.quadTree = new QuadTree<>(boundingBox[0], boundingBox[1], boundingBox[2], boundingBox[3]);
        this.numberTimeBins = numberTimeBins;
    }

    public void add(CumulativeExposureTime cumulativeExposureTime) {

        if (cumulativeExposureTime.getExposureTimes().length != numberTimeBins) {
            return;
        }

        Coord coord = cumulativeExposureTime.getCoord();
        Collection<CumulativeExposureTime> accumulatedExposurePerTimeBinCollection = quadTree.getDisk(coord.getX(), coord.getY(), 0.0);

        if (accumulatedExposurePerTimeBinCollection.isEmpty()) {

            // simply add new array to quadtree
            quadTree.put(coord.getX(), coord.getY(), cumulativeExposureTime);

        } else {

            // the collection should be of length = 1
            CumulativeExposureTime accumulatedExposureTime = accumulatedExposurePerTimeBinCollection.stream().findFirst().get();
            quadTree.remove(coord.getX(), coord.getY(), accumulatedExposureTime);

            for (int i = 0; i < this.numberTimeBins; i++) {
                accumulatedExposureTime.getExposureTimes()[i] += cumulativeExposureTime.getExposureTimes()[i];
            }

            quadTree.put(coord.getX(), coord.getY(), accumulatedExposureTime);

        }
    }

    public final QuadTree<CumulativeExposureTime> getQuadTree() {
        return quadTree;
    }

    public double[] getAverageExposureTimePerTimeBin(int totalNumberOfFacilities) {
        double[] averageExposureTimes = new double[this.numberTimeBins];

        quadTree.values().stream().forEach(cumulativeExposureTime ->
        {
            for (int i = 0; i < this.numberTimeBins; i++) {
                averageExposureTimes[i] += cumulativeExposureTime.getExposureTimes()[i] / totalNumberOfFacilities;
            }

        });

        return averageExposureTimes;
    }
}
