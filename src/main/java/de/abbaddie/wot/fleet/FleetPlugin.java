package de.abbaddie.wot.fleet;

import org.springframework.stereotype.Component;

import de.abbaddie.wot.plugin.AbstractWotPlugin;

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
}