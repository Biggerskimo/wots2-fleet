package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@DiscriminatorValue("navalFormationAttack")
@Entity
public class NavalFormationAttack extends Mission {
	public NavalFormationAttack() {
		super(11, "Verbandsangriff");
	}
	
	@Override
	public String validate(FleetStarter starter) {
		return "Direkte Verbandsangriffe sind nicht erlaubt.";
	}
}
