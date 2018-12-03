package ch.ethz.matsim.external_costs.costs.calculators;

import ch.ethz.matsim.external_costs.costs.factors.EmissionsCostFactors;
import ch.ethz.matsim.external_costs.exposure.ExposureTimeQuadTree;
import ch.ethz.matsim.external_costs.items.Emissions;
import ch.ethz.matsim.external_costs.items.Externality;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.trafficmonitoring.TimeBinUtils;

public class EmissionHealthCostCalculator implements ExternalCostCalculator {
    private final String type = "emissions_health_costs";
    private Scenario scenario;
    private ExposureTimeQuadTree exposureTimeQuadTree;
    private EmissionsCostFactors emissionsCostFactors;

    public EmissionHealthCostCalculator(Scenario scenario, ExposureTimeQuadTree exposureTimeQuadTree, EmissionsCostFactors emissionsCostFactors) {
        this.scenario = scenario;
        this.exposureTimeQuadTree = exposureTimeQuadTree;
        this.emissionsCostFactors = emissionsCostFactors;
    }

    @Override
    public double calculate(Externality externality) {
        double cost = 0.0;
        if (externality instanceof Emissions) {

            double time  = ((Emissions) externality).getTime();

            int currentTimeBin = (int) Math.floor(time / this.exposureTimeQuadTree.getTimeBinSize());

            Coord sourceCoord = scenario.getNetwork().getLinks().get(((Emissions) externality).getLinkId()).getCoord();
            double dose = ((Emissions) externality).getEmissions().get("PM");

            int timeBin = TimeBinUtils.getTimeBinIndex(time, this.exposureTimeQuadTree.getTimeBinSize(), this.exposureTimeQuadTree.getNoTimeBins());

            // multiply exposure time factor
            double exposureTimeFactor = this.exposureTimeQuadTree.getExposureTimeFactorFor(sourceCoord, timeBin);
            cost = (46000/1e6 * exposureTimeFactor + 260000/1e6) * dose;
        }
        return cost;
    }

    @Override
    public String getType() {
        return type;
    }

}
