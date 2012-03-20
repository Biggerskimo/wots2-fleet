package de.abbaddie.wot.fleet.data.spec;

import java.util.List;

import de.abbaddie.wot.data.spec.SpecPredicateAddition;

public interface FleetSpecPredicateAddition extends SpecPredicateAddition {
	public List<FleetDrivePredicate> getDrives();
	
	public int getCapacity();
}
