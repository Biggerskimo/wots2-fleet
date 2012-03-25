package de.abbaddie.wot.fleet.data.spec.impl.tech;

import de.abbaddie.wot.data.spec.SpecComponent;
import de.abbaddie.wot.data.spec.SpecImpl;
import de.abbaddie.wot.data.spec.trait.Technology;
import de.abbaddie.wot.fleet.data.spec.FleetSpecPredicateAddition;
import de.abbaddie.wot.fleet.data.spec.trait.FleetSlotProvider;

@SpecComponent(specId = 108, additions = FleetSpecPredicateAddition.class)
public class ComputerTechnology extends SpecImpl implements FleetSlotProvider, Technology {
	@Override
	public long getSlots() {
		return getLevel() + 1;
	}
}
