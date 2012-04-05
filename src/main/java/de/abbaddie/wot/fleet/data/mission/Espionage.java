package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("espionage")
@Entity
public class Espionage extends Mission {
	public Espionage() {
		super(6, "Spionage");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getTargetPlanet() == null
				|| starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return "Du kannst dich nicht selbst spionieren.";
		}
		
		return null;
	}
}
