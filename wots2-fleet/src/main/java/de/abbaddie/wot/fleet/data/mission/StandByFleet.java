package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("standBy")
@Entity
public class StandByFleet extends Mission {
	public StandByFleet() {
		super(12, "Halten");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		return false;
	}
}
