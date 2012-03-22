package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.spec.trait.Colonizing;
import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("colonize")
@Entity
public class Colonize extends Mission {
	public Colonize() {
		super(9, "Kolonisierung");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getSpecs().filter(Colonizing.class).isEmpty()) {
			return "Zum Kolonisieren werden Kolonieschiffe benötigt!";
		}
		return null;
	}
}
