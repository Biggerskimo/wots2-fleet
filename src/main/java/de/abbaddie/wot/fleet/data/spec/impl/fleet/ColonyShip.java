package de.abbaddie.wot.fleet.data.spec.impl.fleet;

import de.abbaddie.wot.data.spec.SpecComponent;
import de.abbaddie.wot.fleet.data.spec.FleetSpecImpl;
import de.abbaddie.wot.fleet.data.spec.FleetSpecPredicateAddition;
import de.abbaddie.wot.fleet.data.spec.trait.Colonizing;

@SpecComponent(specId = 208, additions = FleetSpecPredicateAddition.class)
public class ColonyShip extends FleetSpecImpl implements Colonizing {
}