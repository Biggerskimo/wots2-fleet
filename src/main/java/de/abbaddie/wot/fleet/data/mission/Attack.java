package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("attack")
@Entity
public class Attack extends Mission {
	public Attack() {
		super(1, "Angriff");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getTargetPlanet() == null || starter.getTargetPlanet().getOwner() == null) {
			return "Du kannst nur Spielerplaneten angreifen.";
		}
		if(starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return "Du kannst dich nicht selbst angreifen.";
		}
		
		return null;
	}
}
