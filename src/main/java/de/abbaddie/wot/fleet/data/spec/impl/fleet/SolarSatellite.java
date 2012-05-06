package de.abbaddie.wot.fleet.data.spec.impl.fleet;

import java.util.HashMap;
import java.util.Map;

import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.planet.PlanetSpecMapper;
import de.abbaddie.wot.data.resource.EnergyProducedResource;
import de.abbaddie.wot.data.resource.ResourcePredicate;
import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.resource.Resources;
import de.abbaddie.wot.data.spec.SpecComponent;
import de.abbaddie.wot.data.spec.trait.Producing;
import de.abbaddie.wot.fleet.data.spec.FleetSpecImpl;

@SpecComponent(name = "solarsatellite")
public class SolarSatellite extends FleetSpecImpl implements Producing {
	@Override
	public ResourceValueSet getProduction() {
		if(!(mapper instanceof PlanetSpecMapper)) {
			throw new IllegalStateException("solar satellite without planet");
		}
		Planet planet = ((PlanetSpecMapper) mapper).getPlanet();
		
		int temp = planet.getTemperature();
		int perSatellite = temp * 4 + 20;
		perSatellite = Math.min(perSatellite, 50);
		
		Map<ResourcePredicate, Double> counts = new HashMap<ResourcePredicate, Double>();
		
		counts.put(new EnergyProducedResource(), (double) (getCount() * perSatellite));
		
		return Resources.generateStatic(counts);
	}
}