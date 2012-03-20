package de.abbaddie.wot.fleet.data.spec;

import javax.persistence.MappedSuperclass;

import de.abbaddie.wot.data.spec.SpecPredicate;

@MappedSuperclass
public interface FleetDrivePredicate {
	public SpecPredicate getDriveSpec();
	
	public int getConsumption();
	
	public int getSpeed();
	
	public double getFactor();
	
	public Integer getMin();
	
	public Integer getMax();
}