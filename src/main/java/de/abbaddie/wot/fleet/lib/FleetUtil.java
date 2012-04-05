package de.abbaddie.wot.fleet.lib;

import java.util.List;

import de.abbaddie.wot.fleet.data.spec.trait.FleetBound.FleetDrive;

public class FleetUtil {
	public static FleetDrive getPreferredDrive(List<FleetDrive> drives) {
		FleetDrive best = null;
		
		for(FleetDrive current : drives) {
			if(best == null || current.getSpeed() > best.getSpeed()) {
				best = current;
			}
		}
		return best;
	}
}
