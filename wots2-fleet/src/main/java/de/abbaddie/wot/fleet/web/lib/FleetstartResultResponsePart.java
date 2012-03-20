package de.abbaddie.wot.fleet.web.lib;

import org.codehaus.jackson.annotate.JsonTypeName;
import org.springframework.validation.BindingResult;

import de.abbaddie.wot.web.lib.ActionResultResponsePart;
import de.abbaddie.wot.web.lib.JsonActionResult;

@JsonTypeName("fleetstartResult")
public class FleetstartResultResponsePart extends ActionResultResponsePart {
	public FleetstartResultResponsePart(JsonActionResult result) {
		super(result);
	}
	
	public FleetstartResultResponsePart(BindingResult result) {
		super(result);
	}
}
