package de.abbaddie.wot.fleet.data.fleet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.abbaddie.wot.data.spec.SpecPredicate;
import de.abbaddie.wot.data.spec.Specs;

@Entity
@Table(name = "ugml_fleet_spec")
class FleetSpec {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fleetSpecID")
	protected int id;
	
	@ManyToOne(targetEntity = FleetImpl.class)
	@JoinColumn(name = "fleetID")
	protected Fleet fleet;
	
	@NotNull
	@Column(name = "specID")
	protected int specId;
	
	private long count;
	
	public FleetSpec() {
		
	}
	
	public FleetSpec(Fleet fleet, SpecPredicate predicate, long count) {
		this.fleet = fleet;
		specId = predicate.getId();
		this.count = count;
	}
	
	public SpecPredicate getSpecPredicate() {
		return Specs.findOne(specId);
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
}
