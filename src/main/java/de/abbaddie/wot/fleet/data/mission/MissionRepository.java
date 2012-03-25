package de.abbaddie.wot.fleet.data.mission;

import java.util.Collection;

public interface MissionRepository {
	public Mission findOne(int missionId);
	
	public Collection<Mission> findAll();
}
