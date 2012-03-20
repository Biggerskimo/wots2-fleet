package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("espionage")
@Entity
public class Espionage extends Mission {
	public Espionage() {
		super(6, "Spionage");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		if(starter.getStartPlanet().getOwner() == starter.getTargetPlanet().getOwner()) {
			return false;
		}
		
		return true;
	}
}
