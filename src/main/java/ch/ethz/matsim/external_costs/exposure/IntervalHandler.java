/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package ch.ethz.matsim.external_costs.exposure;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.trafficmonitoring.TimeBinUtils;

import java.util.HashSet;
import java.util.Set;

public class IntervalHandler implements ActivityStartEventHandler, ActivityEndEventHandler {
	private Scenario scenario;
	private ExposureTimeQuadTree exposureTimeQuadTree;
	private final  Set<Id<Person>> recognisedPersons = new HashSet<>();
	final int timeBinSize;
	final int noTimeBins;
	final double cellSize;


	public IntervalHandler(Scenario scenario, ExposureTimeQuadTree exposureTimeQuadTree){
		this.exposureTimeQuadTree = exposureTimeQuadTree;
		this.scenario = scenario;
		this.timeBinSize = exposureTimeQuadTree.getTimeBinSize();
		this.noTimeBins = exposureTimeQuadTree.getNoTimeBins();
		this.cellSize = exposureTimeQuadTree.getCellSize();
	}

	@Override
	public void reset(int iteration) {
		this.recognisedPersons.clear();
		this.exposureTimeQuadTree.getQuadTree().clear();
	}

	@Override
	public void handleEvent(ActivityEndEvent event) {

		Id<Link> linkId = event.getLinkId();

		// get time bin and time within current interval
        int currentTimeBin = TimeBinUtils.getTimeBinIndex(event.getTime(), timeBinSize, noTimeBins);
		double currentTimeBinUpperLimit = this.timeBinSize * (currentTimeBin + 1);
		double timeWithinCurrentTimeBin = event.getTime() + this.timeBinSize - currentTimeBinUpperLimit;

		// get coordinate of event link
		Coord linkCoord = this.scenario.getNetwork().getLinks().get(linkId).getCoord();

		if(recognisedPersons.contains(event.getPersonId())){

			// update cumulative durations for time interval of activity
			this.exposureTimeQuadTree.add(linkCoord, currentTimeBin, timeWithinCurrentTimeBin - this.timeBinSize);
            currentTimeBin++;

			// update cumulative durations for later time intervals
			while(currentTimeBin < this.noTimeBins){
                this.exposureTimeQuadTree.add(linkCoord, currentTimeBin, -this.timeBinSize);
				currentTimeBin++;
			}

		}else{ // person not yet recognised

            // add to quadtree if nothing yet present
            if (!this.exposureTimeQuadTree.isContaining(linkCoord)) {
                this.exposureTimeQuadTree.addNewExposureItemAt(linkCoord);
            }

			recognisedPersons.add(event.getPersonId());

			// time bins prior to events time bin
			for (int timeBin = 0; timeBin < currentTimeBin; timeBin++) {
                this.exposureTimeQuadTree.add(linkCoord, timeBin, this.timeBinSize);
			}

			// time bin of event
            this.exposureTimeQuadTree.add(linkCoord, currentTimeBin, timeWithinCurrentTimeBin);
		}
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {

		Id<Link> linkId = event.getLinkId();
		this.recognisedPersons.add(event.getPersonId());

		// get time bin and time within current interval
        int currentTimeBin = TimeBinUtils.getTimeBinIndex(event.getTime(), timeBinSize, noTimeBins);
		double currentTimeBinUpperLimit = this.timeBinSize * (currentTimeBin + 1);
		double timeWithinCurrentTimeBin = currentTimeBinUpperLimit - event.getTime();

		// get coordinate of event link
		Coord linkCoord = this.scenario.getNetwork().getLinks().get(linkId).getCoord();

        // add to quadtree if nothing yet present
        if (!this.exposureTimeQuadTree.isContaining(linkCoord)) {
            this.exposureTimeQuadTree.addNewExposureItemAt(linkCoord);
        }

		// update cumulative durations for time interval of activity
		this.exposureTimeQuadTree.add(linkCoord, currentTimeBin, timeWithinCurrentTimeBin);
		currentTimeBin++;

		// update cumulative durations for later time intervals
		while(currentTimeBin < this.noTimeBins){
			this.exposureTimeQuadTree.add(linkCoord, currentTimeBin, this.timeBinSize);
			currentTimeBin++;
		}
	}

    public ExposureTimeQuadTree getExposureTimeQuadTree() {
        return exposureTimeQuadTree;
    }
}
