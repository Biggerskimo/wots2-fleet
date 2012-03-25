package de.abbaddie.wot.fleet.data.start;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.abbaddie.wot.fleet.data.mission.Espionage;

@Service("espionage")
@Scope("prototype")
@FleetStarterConstraint(message = "")
@EspionageFleetStarterConstraint(message = "")
public class EspionageFleetStarterImpl extends FleetStarterImpl {
	public EspionageFleetStarterImpl() {
		super();
		
		setMission(new Espionage());
	}
}
