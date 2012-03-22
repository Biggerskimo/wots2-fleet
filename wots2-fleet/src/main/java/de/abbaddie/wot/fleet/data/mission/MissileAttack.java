package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("missileAttack")
@Entity
public class MissileAttack extends Mission {
	public MissileAttack() {
		super(20, "Interplanetarraketenangriff");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		return "Direkte Interplanetarraketenangriffe sind nicht erlaubt.";
	}
}
