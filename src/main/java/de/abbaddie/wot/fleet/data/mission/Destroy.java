package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("destroy")
@Entity
public class Destroy extends Mission {
	public Destroy() {
		super(5, "Zerstörung");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getTargetPlanet() == null
				|| starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return "Du kannst dich nicht selbst Angreifen.";
		}
		
		return null;
	}
}
