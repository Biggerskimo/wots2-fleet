package de.abbaddie.wot.fleet.data.fleet;

import java.util.Map;

import de.abbaddie.wot.data.coordinates.CoordinatesContainer;
import de.abbaddie.wot.data.event.Event;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.resource.ResourceContainer;
import de.abbaddie.wot.data.spec.SpecContainer;
import de.abbaddie.wot.fleet.data.mission.Mission;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;

public interface Fleet extends CoordinatesContainer, ResourceContainer, SpecContainer<FleetBound> {
	public int getId();
	
	public Planet getStartPlanet();
	
	public Planet getTargetPlanet();
	
	public Mission getMission();
	
	public Map<FleetEventType, Event> getEvents();
}