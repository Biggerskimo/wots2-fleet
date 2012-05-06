package de.abbaddie.wot.fleet;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.stereotype.Component;

import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.fleet.data.ovent.FleetOventImpl;
import de.abbaddie.wot.plugin.AbstractWotPlugin;
import de.abbaddie.wot.plugin.DefaultEntityPlugin;
import de.abbaddie.wot.plugin.EntityPlugin;

@Component
public class FleetPlugin extends AbstractWotPlugin {
	@Override
	public String getName() {
		return "wots2-fleet";
	}
	
	@Override
	public String getVendor() {
		return "Biggerskimo";
	}
	
	@Override
	public String getVersion() {
		return "0.0.1";
	}
	
	@Override
	public Collection<EntityPlugin> getEntityPlugins() {
		return Arrays.<EntityPlugin>asList(new DefaultEntityPlugin(Planet.class, "hangar_jobs"),
				new DefaultEntityPlugin(Planet.class, "hangar_offset", Long.class));
	}
	
	@Override
	public Collection<Class<?>> getMorphiaClasses() {
		return Arrays.<Class<?>>asList(FleetOventImpl.class);
	}
}