package de.abbaddie.wot.fleet.data.fleet;

import de.abbaddie.wot.data.coordinates.EditableCoordinatesContainer;
import de.abbaddie.wot.data.event.Event;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.resource.EditableResourceContainer;
import de.abbaddie.wot.data.spec.EditableSpecContainer;
import de.abbaddie.wot.fleet.data.mission.Mission;

public interface EditableFleet extends Fleet, EditableCoordinatesContainer, EditableResourceContainer,
		EditableSpecContainer {
	public void setStartPlanet(Planet planet);
	
	public void setTargetPlanet(Planet planet);
	
	public void setMission(Mission mission);
	
	public void setEvent(FleetEventType type, Event event);
}
