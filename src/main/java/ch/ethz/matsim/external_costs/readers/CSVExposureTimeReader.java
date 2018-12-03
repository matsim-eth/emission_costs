package ch.ethz.matsim.external_costs.readers;

import ch.ethz.matsim.external_costs.exposure.ExposureItem;
import ch.ethz.matsim.external_costs.exposure.ExposureTimeQuadTree;
import org.matsim.api.core.v01.Coord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CSVExposureTimeReader {
    public ExposureTimeQuadTree read(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

        // get header info to build exposureTimeQuadTree object
        List<String> infoHeader = Arrays.asList(reader.readLine().split(";"));
        List<String> infoEntry = Arrays.asList(reader.readLine().split(";"));

        double[] boundingBox = new double[4];
        boundingBox[0] = Double.parseDouble(infoEntry.get(infoHeader.indexOf("quadTreeBoundingBox_xmin")));
        boundingBox[1] = Double.parseDouble(infoEntry.get(infoHeader.indexOf("quadTreeBoundingBox_ymin")));
        boundingBox[2] = Double.parseDouble(infoEntry.get(infoHeader.indexOf("quadTreeBoundingBox_xmax")));
        boundingBox[3] = Double.parseDouble(infoEntry.get(infoHeader.indexOf("quadTreeBoundingBox_ymax")));

        int timeBinSize = Integer.parseInt(infoEntry.get(infoHeader.indexOf("timeBinSize")));
        int noTimeBins = Integer.parseInt(infoEntry.get(infoHeader.indexOf("noTimeBins")));
        double cellSize = Double.parseDouble(infoEntry.get(infoHeader.indexOf("cellSize")));

        ExposureTimeQuadTree exposureTimeQuadTree = new ExposureTimeQuadTree(boundingBox, timeBinSize, noTimeBins, cellSize);

        // fill in ExposureTimeQuadTree object
        List<String> header = null;
        String line = null;

        while ((line = reader.readLine()) != null) {
            List<String> row = Arrays.asList(line.split(";"));

            if (header == null) {
                header = row;
            } else {
                double x = Double.parseDouble(row.get(header.indexOf("location_x")));
                double y = Double.parseDouble(row.get(header.indexOf("location_y")));
                Coord location = new Coord(x, y);

                double[] exposureTimes = new double[noTimeBins];

                for (int i = 0; i < noTimeBins; i++) {
                    String timebin = "timebin_" + Integer.toString(i);
                    exposureTimes[i] = Double.parseDouble(row.get(header.indexOf(timebin)));
                }

                ExposureItem item = new ExposureItem(exposureTimes, location);
                exposureTimeQuadTree.getQuadTree().put(location.getX(), location.getY(), item);
            }
        }

        reader.close();
        return exposureTimeQuadTree;
    }
}
