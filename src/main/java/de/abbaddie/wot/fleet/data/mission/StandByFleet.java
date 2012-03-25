package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("standBy")
@Entity
public class StandByFleet extends Mission {
	public StandByFleet() {
		super(12, "Halten");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		return "Direktes Halten ist nicht erlaubt.";
	}
}
