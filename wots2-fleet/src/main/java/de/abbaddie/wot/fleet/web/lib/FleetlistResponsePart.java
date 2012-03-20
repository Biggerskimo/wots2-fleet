package de.abbaddie.wot.fleet.web.lib;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeName;

import de.abbaddie.wot.fleet.data.fleet.Fleet;
import de.abbaddie.wot.web.lib.JsonResponsePart;

@JsonTypeName("fleetlist")
public class FleetlistResponsePart extends JsonResponsePart {
	protected List<JsonFleet> fleets;
	
	public FleetlistResponsePart() {
		fleets = new ArrayList<>();
	}
	
	public List<JsonFleet> getFleets() {
		return fleets;
	}
	
	public void addFleets(List<? extends Fleet> fleets) {
		for(Fleet fleet : fleets) {
			this.fleets.add(new JsonFleet(fleet));
		}
	}
}
