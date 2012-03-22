package de.abbaddie.wot.fleet.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.data.spec.filter.PositiveFilter;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;
import de.abbaddie.wot.fleet.data.start.FleetStarter;
import de.abbaddie.wot.fleet.web.lib.FleetstartResponsePart;
import de.abbaddie.wot.fleet.web.lib.FleetstartResultResponsePart;
import de.abbaddie.wot.web.LoggedInController;
import de.abbaddie.wot.web.lib.JsonResponse;
import de.abbaddie.wot.web.lib.JsonResponsePart;

@Controller
@RequestMapping("/ajax")
public class FleetStart extends LoggedInController {
	@Autowired
	protected AutowireCapableBeanFactory factory;
	
	@ModelAttribute("starter")
	public FleetStarter getStarter() {
		FleetStarter starter = factory.getBean("default", FleetStarter.class);
		starter.setStartPlanet(getCurrentPlanet());
		return starter;
	}
	
	public SpecSet<FleetBound> getFleetSpecs() {
		return getCurrentPlanet().getSpecs().filter(new PositiveFilter()).filter(FleetBound.class);
	}
	
	@RequestMapping(value = "fleetstart/fire")
	@ResponseBody
	public JsonResponse start(@ModelAttribute("starter") @Valid FleetStarter starter, BindingResult result) {
		JsonResponse resp = new JsonResponse();
		addDefaultParts(resp);
		
		if(!result.hasErrors()) {
			result = starter.validateAndFire();
		}
		
		JsonResponsePart part = new FleetstartResultResponsePart(result);
		resp.addPart(part);
		
		part = new FleetstartResponsePart(getFleetSpecs());
		resp.addPart(part);
		
		return resp;
	}
	
	@RequestMapping(value = "fleetstart")
	@ResponseBody
	public JsonResponse show() {
		JsonResponse resp = new JsonResponse();
		addDefaultParts(resp);
		
		FleetstartResponsePart part = new FleetstartResponsePart(getFleetSpecs());
		resp.addPart(part);
		
		return resp;
	}
}
