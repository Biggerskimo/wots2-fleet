package de.abbaddie.wot.fleet.data.spec;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ugml_spec")
public class FleetSpecPredicateAdditionImplEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "specID")
	protected int id;
	
	@OneToMany(targetEntity = FleetDrivePredicateImpl.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "specID")
	protected List<FleetDrivePredicate> drives;
	
	protected int capacity;
	
	public int getId() {
		return id;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public List<FleetDrivePredicate> getDrives() {
		return drives;
	}
}