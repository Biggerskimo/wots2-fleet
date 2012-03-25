package de.abbaddie.wot.fleet.data.mission;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository
public class MissionRepositoryImpl implements MissionRepository {
	protected Map<Integer, Mission> missions;
	
	@PersistenceContext
	protected EntityManager em;
	
	@Override
	public Mission findOne(int missionId) {
		findAll();
		
		return missions.get(missionId);
	}
	
	@Override
	public Collection<Mission> findAll() {
		if(missions == null) {
			TypedQuery<Mission> q = em.createQuery("SELECT m FROM Mission m", Mission.class);
			List<Mission> list = q.getResultList();
			missions = new HashMap<>(list.size());
			
			for(Mission mission : list) {
				missions.put(mission.getMissionId(), mission);
			}
		}
		return missions.values();
	}
}
