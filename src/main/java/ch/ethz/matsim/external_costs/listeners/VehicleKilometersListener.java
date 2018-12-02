package ch.ethz.matsim.external_costs.listeners;

import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.VehicleLeavesTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.events.handler.VehicleLeavesTrafficEventHandler;

public class VehicleKilometersListener implements VehicleEntersTrafficEventHandler, VehicleLeavesTrafficEventHandler, LinkEnterEventHandler, LinkLeaveEventHandler {
    @Override
    public void handleEvent(LinkEnterEvent event) {

    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {

    }

    @Override
    public void handleEvent(VehicleEntersTrafficEvent event) {

    }

    @Override
    public void handleEvent(VehicleLeavesTrafficEvent event) {

    }

    @Override
    public void reset(int iteration) {

    }
}
