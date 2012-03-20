package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.ConstraintValidatorContext;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("missileAttack")
@Entity
public class MissileAttack extends Mission {
	public MissileAttack() {
		super(20, "Interplanetarraketenangriff");
	}
	
	@Override
	public boolean validate(FleetStarter starter, ConstraintValidatorContext context) {
		return false;
	}
}
