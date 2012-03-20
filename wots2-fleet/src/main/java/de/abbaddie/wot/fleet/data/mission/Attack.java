package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("attack")
@Entity
public class Attack extends Mission {
	public Attack() {
		super(1, "Angriff");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		if(starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return false;
		}
		
		return true;
	}
}
