package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("deploy")
@Entity
public class Deploy extends Mission {
	public Deploy() {
		super(4, "Stationierung");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		if(starter.getStartPlanet().getOwner() != starter.getTargetPlanet().getOwner()) {
			return false;
		}
		
		return true;
	}
}
