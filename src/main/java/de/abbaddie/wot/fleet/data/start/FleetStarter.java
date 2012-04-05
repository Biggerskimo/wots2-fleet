package de.abbaddie.wot.fleet.data.start;

import java.util.List;
import java.util.Map;

import org.joda.time.Duration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import de.abbaddie.wot.data.coordinates.CoordinatesContainer;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.resource.ResourceContainer;
import de.abbaddie.wot.data.resource.ResourcePredicate;
import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.spec.SpecContainer;
import de.abbaddie.wot.fleet.data.mission.Mission;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;

public interface FleetStarter extends CoordinatesContainer, ResourceContainer, SpecContainer<FleetBound> {
	@Transactional
	public BindingResult validateAndFire();
	
	@Transactional
	public void fire();
	
	public List<Mission> getAllowedMissions();
	
	@Override
	public ResourcePredicate[] getAllowedResourcePredicates();
	
	public double getCapacity();
	
	public ResourceValueSet getConsumption();
	
	public int getDistance();
	
	public Duration getDuration();
	
	public int getSpeed();
	
	public ResourceValueSet getUsedResources();
	
	public Planet getTargetPlanet();
	
	public Map<String, String> getLevels();
	
	public Mission getMission();
	
	public int getMissionId();
	
	public Map<String, String> getResourceCounts();
	
	public double getSpeedFactor();
	
	public Planet getStartPlanet();
	
	// setter
	public void setMissionId(int missionId);
	
	public void setMission(Mission mission);
	
	public void setTargetPlanetId(int targetPlanetId);
	
	public void setSpeedFactor(double speed);
	
	public void setStartPlanet(Planet startPlanet);
	
}