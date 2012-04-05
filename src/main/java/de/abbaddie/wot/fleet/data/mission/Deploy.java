package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("deploy")
@Entity
public class Deploy extends Mission {
	public Deploy() {
		super(4, "Stationierung");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		if(starter.getTargetPlanet() == null
				|| starter.getStartPlanet().getOwner() != starter.getTargetPlanet().getOwner()) {
			return "Du kannst nur auf deine eigenen Planeten stationieren.";
		}
		
		return null;
	}
}
