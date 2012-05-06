package de.abbaddie.wot.fleet.data.ovent;

import com.google.code.morphia.annotations.Entity;

import de.abbaddie.wot.data.coordinates.Coordinates;
import de.abbaddie.wot.data.ovent.OventImpl;
import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.web.lib.JsonCoordinates;
import de.abbaddie.wot.web.lib.JsonResourceValueSet;
import de.abbaddie.wot.web.lib.JsonSpecSet;

@Entity
public class FleetOventImpl extends OventImpl {
	protected FleetOventFleet fleet;
	
	@Override
	public String getType() {
		return "ovent";
	}
	
	public void setFleet(FleetOventFleet fleet) {
		this.fleet = fleet;
	}
	
	public static class FleetOventFleet {
		private int fleetId;
		private int ownerId;
		private int ofiaraId;
		private int startPlanetId;
		private int targetPlanetId;
		private JsonResourceValueSet resources;
		private JsonCoordinates startCoords;
		private JsonCoordinates targetCoords;
		private JsonSpecSet specs;
		private String cssClass;
		private int missionId;
		private String startPlanetName;
		private String targetPlanetName;
		private String passage;
		
		public FleetOventFleet() {
			
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
		
		public ResourceValueSet getResources() {
			return resources.toJavaSet();
		}
		
		public Coordinates getStartCoords() {
			return startCoords;
		}
		
		public Coordinates getTargetCoords() {
			return targetCoords;
		}
		
		public SpecSet<?> getSpecs() {
			return specs.toJavaSet();
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
		
		public void setCssClass(String cssClass) {
			this.cssClass = cssClass;
		}
		
		public void setFleetId(int fleetId) {
			this.fleetId = fleetId;
		}
		
		public void setMissionId(int missionId) {
			this.missionId = missionId;
		}
		
		public void setOfiaraId(int ofiaraId) {
			this.ofiaraId = ofiaraId;
		}
		
		public void setOwnerId(int ownerId) {
			this.ownerId = ownerId;
		}
		
		public void setPassage(String passage) {
			this.passage = passage;
		}
		
		public void setResources(ResourceValueSet resources) {
			this.resources = new JsonResourceValueSet(resources);
		}
		
		public void setSpecs(SpecSet<?> specs) {
			this.specs = new JsonSpecSet(specs);
		}
		
		public void setStartCoords(Coordinates startCoords) {
			this.startCoords = new JsonCoordinates(startCoords);
		}
		
		public void setStartPlanetId(int startPlanetId) {
			this.startPlanetId = startPlanetId;
		}
		
		public void setStartPlanetName(String startPlanetName) {
			this.startPlanetName = startPlanetName;
		}
		
		public void setTargetCoords(Coordinates targetCoords) {
			this.targetCoords = new JsonCoordinates(targetCoords);
		}
		
		public void setTargetPlanetId(int targetPlanetId) {
			this.targetPlanetId = targetPlanetId;
		}
		
		public void setTargetPlanetName(String targetPlanetName) {
			this.targetPlanetName = targetPlanetName;
		}
	}
}
