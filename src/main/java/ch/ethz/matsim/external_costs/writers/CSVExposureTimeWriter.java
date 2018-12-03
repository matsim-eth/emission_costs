package ch.ethz.matsim.external_costs.writers;

import ch.ethz.matsim.external_costs.exposure.ExposureItem;
import ch.ethz.matsim.external_costs.exposure.ExposureTimeQuadTree;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVExposureTimeWriter {
    private ExposureTimeQuadTree exposureTimeQuadTree;

    public CSVExposureTimeWriter(ExposureTimeQuadTree exposureTimeQuadTree) {
        this.exposureTimeQuadTree = exposureTimeQuadTree;
    }

    public void write(String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)));

        writer.write(formatInfo() + "\n");
        writer.flush();

        writer.write(formatHeader() + "\n");
        writer.flush();

        for (ExposureItem exposureItem : this.exposureTimeQuadTree.getQuadTree().values()) {
            writer.write(formatItem(exposureItem) + "\n");
            writer.flush();
        }

        writer.flush();
        writer.close();

    }

    private String formatInfo() {

        String header = String.join(";", new String[]{
                "timeBinSize",
                "noTimeBins",
                "cellSize",
                "quadTreeBoundingBox_xmin",
                "quadTreeBoundingBox_ymin",
                "quadTreeBoundingBox_xmax",
                "quadTreeBoundingBox_ymax"
        });

        String entry = String.join(";", new String[]{
                String.valueOf(exposureTimeQuadTree.getTimeBinSize()),
                String.valueOf(exposureTimeQuadTree.getNoTimeBins()),
                String.valueOf(exposureTimeQuadTree.getCellSize()),
                String.valueOf(exposureTimeQuadTree.getQuadTree().getMinEasting()),
                String.valueOf(exposureTimeQuadTree.getQuadTree().getMinNorthing()),
                String.valueOf(exposureTimeQuadTree.getQuadTree().getMaxEasting()),
                String.valueOf(exposureTimeQuadTree.getQuadTree().getMaxNorthing())
        });

        return String.join("\n", new String[]{header, entry});
    }

    private String formatHeader() {
        String[] header = new String[2 + this.exposureTimeQuadTree.getNoTimeBins()];
        header[0] = "location_x";
        header[1] = "location_y";
        for (int i = 0; i < this.exposureTimeQuadTree.getNoTimeBins(); i++) {
            header[i+2] = "timebin_" + String.valueOf(i);
        }
        return String.join(";", header);
    }

    private String formatItem(ExposureItem exposureItem) {
        String[] entry = new String[2 + this.exposureTimeQuadTree.getNoTimeBins()];
        entry[0] = String.valueOf(exposureItem.getCoord().getX());
        entry[1] = String.valueOf(exposureItem.getCoord().getY());
        for (int i = 0; i < this.exposureTimeQuadTree.getNoTimeBins(); i++) {
            entry[i+2] = String.valueOf(exposureItem.getExposureTimes()[i]);
        }
        return String.join(";", entry);
    }
}
