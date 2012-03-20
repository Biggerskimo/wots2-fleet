package de.abbaddie.wot.fleet.web.lib;

import java.util.Map;

import org.joda.time.DateTime;

import de.abbaddie.wot.data.event.Event;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.fleet.data.fleet.DefaultEventTypes;
import de.abbaddie.wot.fleet.data.fleet.Fleet;
import de.abbaddie.wot.fleet.data.fleet.FleetEventType;
import de.abbaddie.wot.web.lib.JsonCoordinates;
import de.abbaddie.wot.web.lib.JsonPlanet;

public class JsonFleet {
	protected int fleetId;
	protected int missionId;
	protected JsonPlanet startPlanet;
	protected JsonPlanet targetPlanet;
	protected DateTime impactTime;
	protected DateTime returnTime;
	
	public JsonFleet(Fleet fleet) {
		fleetId = fleet.getId();
		missionId = fleet.getMission().getMissionId();
		startPlanet = new JsonPlanet(fleet.getStartPlanet());
		
		Planet target = fleet.getTargetPlanet();
		if(target == null) {
			targetPlanet = new JsonPlanet(0, 0, Planet.NO_PLANET_NAME, new JsonCoordinates(fleet.getCoordinates()));
		} else {
			targetPlanet = new JsonPlanet(fleet.getTargetPlanet());
		}
		Map<FleetEventType, Event> events = fleet.getEvents();
		impactTime = events.get(DefaultEventTypes.IMPACT) == null ? null : events.get(DefaultEventTypes.IMPACT)
				.getTime();
		returnTime = fleet.getEvents().get(DefaultEventTypes.RETURN).getTime();
	}
	
	public int getFleetId() {
		return fleetId;
	}
	
	public int getMissionId() {
		return missionId;
	}
	
	public JsonPlanet getStartPlanet() {
		return startPlanet;
	}
	
	public DateTime getImpactTime() {
		return impactTime;
	}
	
	public DateTime getReturnTime() {
		return returnTime;
	}
	
	public JsonPlanet getTargetPlanet() {
		return targetPlanet;
	}
}
