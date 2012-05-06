package de.abbaddie.wot.fleet.data.event;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.abbaddie.wot.data.event.EventExecutor;
import de.abbaddie.wot.data.event.EventImpl;
import de.abbaddie.wot.fleet.data.fleet.Fleet;
import de.abbaddie.wot.fleet.data.fleet.FleetImpl;

@DiscriminatorValue("fleet")
@Entity
public class FleetEventImpl extends EventImpl {
	@ManyToOne(targetEntity = FleetImpl.class)
	@JoinColumn(name = "fleetId")
	protected Fleet fleet;
	
	public Fleet getFleet() {
		return fleet;
	}
	
	public void setFleet(Fleet fleet) {
		this.fleet = fleet;
	}
	
	@Override
	public EventExecutor getExecutor() {
		return new FleetEventExecutor(this);
	}
}
