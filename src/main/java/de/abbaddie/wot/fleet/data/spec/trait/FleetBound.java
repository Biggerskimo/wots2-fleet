package de.abbaddie.wot.fleet.data.spec.trait;

import java.util.List;

import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.data.spec.trait.PlanetBound;

public interface FleetBound extends PlanetBound {
	public List<FleetDrive> getDrives(SpecSet<?> technologies);
	
	public double getCapacity();
	
	public static interface FleetDrive {
		public FleetBound getFleetSpec();
		
		public double getConsumption();
		
		public int getSpeed();
	}
}
