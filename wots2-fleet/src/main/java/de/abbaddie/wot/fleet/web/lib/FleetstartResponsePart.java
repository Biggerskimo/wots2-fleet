package de.abbaddie.wot.fleet.web.lib;

import org.codehaus.jackson.annotate.JsonTypeName;

import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.web.lib.JsonResponsePart;
import de.abbaddie.wot.web.lib.JsonSpecSet;

@JsonTypeName("fleetstart")
public class FleetstartResponsePart extends JsonResponsePart {
	protected JsonSpecSet specs;
	
	public FleetstartResponsePart(JsonSpecSet specs) {
		this.specs = specs;
	}
	
	public FleetstartResponsePart(SpecSet<?> specs) {
		this.specs = new JsonSpecSet(specs);
	}
	
	public JsonSpecSet getSpecs() {
		return specs;
	}
}
