package de.abbaddie.wot.fleet.web.lib;

import java.util.ArrayList;
import java.util.List;

import de.abbaddie.wot.data.ovent.Ovent;
import de.abbaddie.wot.fleet.data.ovent.FleetOventNature;
import de.abbaddie.wot.fleet.data.ovent.FleetOventNature.FleetOventFleet;
import de.abbaddie.wot.web.lib.JsonCoordinates;
import de.abbaddie.wot.web.lib.JsonOvent;
import de.abbaddie.wot.web.lib.JsonOventComponent;
import de.abbaddie.wot.web.lib.JsonResourceValueSet;
import de.abbaddie.wot.web.lib.JsonSpecSet;

@JsonOventComponent(type = "fleet", nature = FleetOventNature.class)
public class JsonFleetOvent extends JsonOvent {
	protected List<JsonFleetOventFleet> fleets;
	
	public JsonFleetOvent() {
		fleets = new ArrayList<>();
	}
	
	public List<JsonFleetOventFleet> getFleets() {
		return fleets;
	}
	
	@Override
	protected void setOvent(Ovent ovent) {
		for(FleetOventFleet fleet : ((FleetOventNature) ovent.getNature()).getFleets()) {
			fleets.add(new JsonFleetOventFleet(fleet));
		}
	}
	
	public static class JsonFleetOventFleet {
		private int fleetId;
		private int ownerId;
		private int ofiaraId;
		private int startPlanetId;
		private int targetPlanetId;
		private String cssClass;
		private int missionId;
		private String startPlanetName;
		private String targetPlanetName;
		private String passage;
		
		private JsonSpecSet specs;
		private JsonCoordinates startCoords;
		private JsonCoordinates targetCoords;
		private JsonResourceValueSet resources;
		
		public JsonFleetOventFleet(FleetOventFleet fleet) {
			fleetId = fleet.getFleetId();
			ownerId = fleet.getOwnerId();
			ofiaraId = fleet.getOfiaraId();
			startPlanetId = fleet.getStartPlanetId();
			targetPlanetId = fleet.getTargetPlanetId();
			cssClass = fleet.getCssClass();
			missionId = fleet.getMissionId();
			startPlanetName = fleet.getStartPlanetName();
			targetPlanetName = fleet.getTargetPlanetName();
			passage = fleet.getPassage();
			
			specs = new JsonSpecSet(fleet.getSpecs());
			startCoords = new JsonCoordinates(fleet.getStartCoords());
			targetCoords = new JsonCoordinates(fleet.getTargetCoords());
			resources = new JsonResourceValueSet(fleet.getResources());
		}
		
		public int getFleetId() {
			return fleetId;
		}
		
		public int getOwnerId() {
			return ownerId;
		}
		
		public int getOfiaraId() {
			return ofiaraId;
		}
		
		public int getStartPlanetId() {
			return startPlanetId;
		}
		
		public int getTargetPlanetId() {
			return targetPlanetId;
		}
		
		public String getCssClass() {
			return cssClass;
		}
		
		public int getMissionId() {
			return missionId;
		}
		
		public String getStartPlanetName() {
			return startPlanetName;
		}
		
		public String getTargetPlanetName() {
			return targetPlanetName;
		}
		
		public String getPassage() {
			return passage;
		}
		
		public JsonSpecSet getSpecs() {
			return specs;
		}
		
		public JsonCoordinates getStartCoords() {
			return startCoords;
		}
		
		public JsonCoordinates getTargetCoords() {
			return targetCoords;
		}
		
		public JsonResourceValueSet getResources() {
			return resources;
		}
	}
}
