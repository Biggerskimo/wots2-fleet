package de.abbaddie.wot.fleet.web.lib;

import org.codehaus.jackson.annotate.JsonTypeName;

import de.abbaddie.wot.web.lib.ActionResultResponsePart;

@JsonTypeName("fleetlistCancelResult")
public class FleetlistCancelResultResponsePart extends ActionResultResponsePart {
	public FleetlistCancelResultResponsePart() {
		super();
	}
	
	public FleetlistCancelResultResponsePart(String... errors) {
		super(errors);
	}
}
