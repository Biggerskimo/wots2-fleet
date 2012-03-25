package de.abbaddie.wot.fleet.data.spec;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.abbaddie.wot.data.spec.SpecPredicate;
import de.abbaddie.wot.data.spec.SpecPredicateImpl;

@Entity
@Table(name = "ugml_spec_drive")
public class FleetDrivePredicateImpl implements FleetDrivePredicate {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "specDriveID")
	protected int id;
	
	@ManyToOne(targetEntity = SpecPredicateImpl.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "specID")
	protected SpecPredicate fleetSpec;
	
	@ManyToOne(targetEntity = SpecPredicateImpl.class)
	@JoinColumn(name = "drive")
	protected SpecPredicate driveSpec;
	
	protected int speed;
	
	protected int consumption;
	
	protected double factor;
	
	protected Integer min;
	protected Integer max;
	
	@Override
	public SpecPredicate getDriveSpec() {
		return driveSpec;
	}
	
	@Override
	public int getConsumption() {
		return consumption;
	}
	
	@Override
	public int getSpeed() {
		return speed;
	}
	
	@Override
	public double getFactor() {
		return factor;
	}
	
	@Override
	public Integer getMin() {
		return min;
	}
	
	@Override
	public Integer getMax() {
		return max;
	}
}