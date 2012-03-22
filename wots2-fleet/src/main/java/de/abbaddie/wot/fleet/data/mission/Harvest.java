package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.spec.trait.Harvesting;
import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("harvest")
@Entity
public class Harvest extends Mission {
	public Harvest() {
		super(6, "Abbau");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getSpecs().filter(Harvesting.class).isEmpty()) {
			return "Du benötigst Recycler zum Abbauen!";
		}
		return null;
	}
}
