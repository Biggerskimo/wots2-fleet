package de.abbaddie.wot.fleet.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.abbaddie.wot.auth.AuthenticationSystem;
import de.abbaddie.wot.config.Config;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.fleet.data.spec.FleetSpecPredicateAddition;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound.FleetDrive;
import de.abbaddie.wot.fleet.lib.FleetUtil;
import de.abbaddie.wot.web.view.MainPageMutator;

@Service
public class FleetMainPageMutator implements MainPageMutator {
	@Autowired
	protected Config config;
	
	@Autowired
	protected AuthenticationSystem auth;
	
	@Override
	public String getHeadStr() {
		SpecSet<FleetBound> specs = auth.getCurrentPlanet().getSpecs().filter(FleetBound.class);
		SpecSet<?> unified = auth.getCurrentPlanet().getUnifiedSpecs();
		
		StringBuilder builder = new StringBuilder("<script type=\"text/javascript\">");
		builder.append("WotFleetstartDetails.globalFactor=");
		builder.append(Integer.valueOf(config.get("fleet_speed")) / 2500);
		builder.append(";WotFleetstartDetails.specs={");
		for(FleetBound spec : specs) {
			builder.append(spec.getPredicate().getId());
			
			FleetDrive drive = FleetUtil.getPreferredDrive(spec.getDrives(unified));
			if(drive == null) {
				builder.append(":null,");
			} else {
				builder.append(":[");
				builder.append(drive.getSpeed());
				builder.append(',');
				builder.append(drive.getPredicate().getConsumption());
				builder.append(',');
				builder.append(spec.getPredicate().getAddition(FleetSpecPredicateAddition.class).getCapacity());
				builder.append("],");
			}
		}
		builder.append("}</script>");
		return builder.toString();
	}
	
}
