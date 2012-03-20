package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("navalFormationAttack")
@Entity
public class NavalFormationAttack extends Mission {
	public NavalFormationAttack() {
		super(11, "Verbandsangriff");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		return false;
	}
}
