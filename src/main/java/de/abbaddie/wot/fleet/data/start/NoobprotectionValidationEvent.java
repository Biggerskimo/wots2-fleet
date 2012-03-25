package de.abbaddie.wot.fleet.data.start;

import de.abbaddie.wot.event.EventListener;
import de.abbaddie.wot.event.ManagedEventListener;
import de.abbaddie.wot.fleet.lib.NoobProtectionCheck;

@ManagedEventListener(event = FleetstartValidationEvent.class)
public class NoobprotectionValidationEvent implements EventListener<FleetStarter, String> {
	@Override
	public String run(FleetStarter starter) {
		if(starter.getTargetPlanet() != null
				&& starter.getTargetPlanet().getOwner() != null
				&& NoobProtectionCheck.facesProtection(starter.getStartPlanet().getOwner(), starter.getTargetPlanet()
						.getOwner())) {
			return "Das Ziel befindet sich im Noobschutz.";
		}
		return null;
	}
}