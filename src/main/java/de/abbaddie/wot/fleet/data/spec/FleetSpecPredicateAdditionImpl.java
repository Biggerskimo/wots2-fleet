package de.abbaddie.wot.fleet.data.spec;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.abbaddie.wot.data.spec.SpecPredicate;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FleetSpecPredicateAdditionImpl implements FleetSpecPredicateAddition {
	protected FleetSpecPredicateAdditionImplEntity entity;
	
	@PersistenceContext
	protected EntityManager em;
	
	@Override
	public void setPredicate(SpecPredicate predicate) {
		int specId = predicate.getId();
		
		entity = em.find(FleetSpecPredicateAdditionImplEntity.class, specId);
	}
	
	@Override
	public List<FleetDrivePredicate> getDrives() {
		return entity.getDrives();
	}
	
	@Override
	public int getCapacity() {
		return entity.getCapacity();
	}
}
