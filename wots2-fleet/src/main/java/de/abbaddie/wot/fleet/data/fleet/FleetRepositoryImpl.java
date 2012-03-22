package de.abbaddie.wot.fleet.data.fleet;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.abbaddie.wot.data.user.WotUser;

@Repository
public class FleetRepositoryImpl implements FleetRepository {
	@PersistenceContext
	protected EntityManager em;
	
	@Override
	public List<? extends FleetImpl> findByOwner(WotUser owner) {
		TypedQuery<FleetImpl> q = em.createQuery("SELECT f FROM FleetImpl f WHERE f.ownerId = :ownerId",
				FleetImpl.class);
		q.setParameter("ownerId", owner.getId());
		return q.getResultList();
	}
	
	@Override
	public Fleet findOne(int fleetId) {
		return em.find(FleetImpl.class, fleetId);
	}
	
	@Override
	public EditableFleet create() {
		EditableFleet fleet = new FleetImpl();
		em.persist(fleet);
		
		return fleet;
	}
	
	@Deprecated
	@Override
	public void save(EditableFleet fleet) {
		try {
			em.persist(fleet);
		} catch(EntityExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void update(EditableFleet fleet) {
		em.merge(fleet);
	}
	
	@Override
	public void refresh(EditableFleet fleet) {
		em.refresh(fleet);
	}
}
