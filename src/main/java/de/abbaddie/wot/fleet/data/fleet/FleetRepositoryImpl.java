package de.abbaddie.wot.fleet.data.fleet;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.abbaddie.wot.data.spec.SpecRepository;
import de.abbaddie.wot.data.user.User;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;

@Repository
public class FleetRepositoryImpl implements FleetRepository {
	@PersistenceContext
	protected EntityManager em;
	
	@Autowired
	protected SpecRepository specRepo;
	
	@Override
	public List<? extends FleetImpl> findByOwner(User owner) {
		TypedQuery<FleetImpl> q = em.createQuery(
				"SELECT f FROM FleetImpl f JOIN FETCH f.startPlanet JOIN FETCH f.targetPlanet"
						+ " JOIN FETCH f.impactEvent JOIN FETCH f.returnEvent WHERE f.ownerId = :ownerId",
				FleetImpl.class);
		q.setParameter("ownerId", owner.getId());
		List<FleetImpl> list = q.getResultList();
		
		for(FleetImpl f : list) {
			postprocess(f);
		}
		return list;
	}
	
	@Override
	public Fleet findOne(int fleetId) {
		FleetImpl f = em.find(FleetImpl.class, fleetId);
		postprocess(f);
		
		return f;
	}
	
	@Override
	public EditableFleet create() {
		FleetImpl fleet = new FleetImpl();
		
		postprocess(fleet);
		em.persist(fleet);
		
		return fleet;
	}
	
	protected void postprocess(FleetImpl fleet) {
		fleet.setSpecsInternal(specRepo.getSpecSet(fleet.new FleetSpecMapper()).filter(FleetBound.class));
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
