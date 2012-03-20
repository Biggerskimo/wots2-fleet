package de.abbaddie.wot.fleet.data.mission;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import de.abbaddie.wot.lib.BeanConfigurationUtil;

public final class Missions {
	protected static MissionRepository repo;
	
	protected static Missions instance = new Missions();
	
	@Autowired
	protected MissionRepository repo_;
	
	private Missions() {
		BeanConfigurationUtil.configure(this);
		
		repo = repo_;
	}
	
	public static Mission findOne(int missionId) {
		return repo.findOne(missionId);
	}
	
	public static Collection<Mission> findAll() {
		return repo.findAll();
	}
}
