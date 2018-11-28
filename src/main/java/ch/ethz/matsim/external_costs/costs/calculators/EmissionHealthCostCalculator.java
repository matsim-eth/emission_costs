package ch.ethz.matsim.external_costs.costs.calculators;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;
import ch.ethz.matsim.external_costs.dispersion.DispersionModel;
import ch.ethz.matsim.external_costs.items.*;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.network.NetworkUtils;

import java.util.Collection;

public class EmissionHealthCostCalculator implements ExternalCostCalculator {
    private final String type = "emissions_health_costs";
    private Scenario scenario;
    private ExposureItemFactory exposureItemFactory;
    private EmissionsCostFactors emissionsCostFactors;
    private CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree;
    private DispersionModel dispersionModel;
    private double[] averageExposurePerTimeBin;
    private double distance;

    public EmissionHealthCostCalculator(Scenario scenario, ExposureItemFactory exposureItemFactory, EmissionsCostFactors emissionsCostFactors, CumulativeExposureTimeQuadTree cumulativeExposureTimeQuadTree, DispersionModel dispersionModel, double distance) {
        this.scenario = scenario;
        this.exposureItemFactory = exposureItemFactory;
        this.emissionsCostFactors = emissionsCostFactors;
        this.cumulativeExposureTimeQuadTree = cumulativeExposureTimeQuadTree;
        this.dispersionModel = dispersionModel;
        this.distance = distance;
        int numberFacilities = scenario.getActivityFacilities().getFacilities().size();
        this.averageExposurePerTimeBin = this.cumulativeExposureTimeQuadTree.getAverageExposureTimePerTimeBin(numberFacilities);
    }

    @Override
    public double calculate(Externality externality) {
        double cost = 0.0;
        if (externality instanceof Emissions) {
            double time  = ((Emissions) externality).getTime();
            int timeBin = this.exposureItemFactory.getTimeBin(time);
            Coord sourceCoord = scenario.getNetwork().getLinks().get(((Emissions) externality).getLinkId()).getCoord();
            double sourceValue = ((Emissions) externality).getEmissions().get("PM");

            Collection<CumulativeExposureTime> exposureCollection = this.cumulativeExposureTimeQuadTree.getQuadTree().getDisk(sourceCoord.getX(), sourceCoord.getY(), this.distance);

            for (CumulativeExposureTime cumulativeExposureTime : exposureCollection) {
                Coord receptionCoord = cumulativeExposureTime.getCoord();
                double r = NetworkUtils.getEuclideanDistance(sourceCoord, receptionCoord);
                cost += sourceValue * dispersionModel.calculate(r) * cumulativeExposureTime.getExposureTimes()[timeBin];
            }

            cost /= this.averageExposurePerTimeBin[timeBin];
            cost *= this.emissionsCostFactors.getCostFactor("PM");
        }
        return cost;
    }

    @Override
    public String getType() {
        return type;
    }
}
