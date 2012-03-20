package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("destroy")
@Entity
public class Destroy extends Mission {
	public Destroy() {
		super(5, "Zerstörung");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		if(starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return false;
		}
		
		return true;
	}
}
