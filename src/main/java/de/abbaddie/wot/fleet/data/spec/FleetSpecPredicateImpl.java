package de.abbaddie.wot.fleet.data.spec;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import de.abbaddie.wot.data.spec.SpecPredicateImpl;

@Entity
@DiscriminatorValue("fleet")
public class FleetSpecPredicateImpl extends SpecPredicateImpl {
	protected int capacity;
	
	@OneToMany(targetEntity = FleetDrivePredicateImpl.class)
	@JoinColumn(name = "specId")
	protected List<FleetDrivePredicate> drives;
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public List<FleetDrivePredicate> getDrives() {
		return drives;
	}
	
	public void setDrives(List<FleetDrivePredicate> drives) {
		this.drives = drives;
	}
}
