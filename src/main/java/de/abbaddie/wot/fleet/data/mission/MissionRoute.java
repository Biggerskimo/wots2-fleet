package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Cacheable
@Entity
@Table(name = "ugml_mission_route")
public class MissionRoute {
	@Column(name = "routeID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	protected int routeId;
	
	@Column(name = "startPlanetTypeID")
	protected Integer startPlanetTypeId;
	
	@Column(name = "endPlanetTypeID")
	protected Integer targetPlanetTypeId;
	
	protected boolean noobProtection;
	
	@Override
	public String toString() {
		return startPlanetTypeId + "->" + targetPlanetTypeId;
	}
	
	public Integer getStartPlanetTypeId() {
		return startPlanetTypeId;
	}
	
	public Integer getTargetPlanetTypeId() {
		return targetPlanetTypeId;
	}
	
	public boolean getNoobProtection() {
		return noobProtection;
	}
}
