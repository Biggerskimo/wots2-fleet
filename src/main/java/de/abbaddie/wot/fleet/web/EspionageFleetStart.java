package de.abbaddie.wot.fleet.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.abbaddie.wot.fleet.data.start.FleetStarter;
import de.abbaddie.wot.fleet.web.lib.FleetstartResultResponsePart;
import de.abbaddie.wot.web.LoggedInController;
import de.abbaddie.wot.web.lib.ActionResultResponsePart;
import de.abbaddie.wot.web.lib.JsonResponse;

@Controller
@RequestMapping("/ajax")
public class EspionageFleetStart extends LoggedInController {
	@Autowired
	protected AutowireCapableBeanFactory factory;
	
	@ModelAttribute("starter")
	public FleetStarter getStarter() {
		FleetStarter starter = factory.getBean("espionage", FleetStarter.class);
		starter.setStartPlanet(getCurrentPlanet());
		return starter;
	}
	
	@RequestMapping(value = "fleetstart/fire/espionage")
	@ResponseBody
	public JsonResponse start(@ModelAttribute("starter") @Valid FleetStarter starter, BindingResult result) {
		JsonResponse resp = new JsonResponse();
		addDefaultParts(resp);
		
		if(!result.hasErrors()) {
			result = starter.validateAndFire();
		}
		
		ActionResultResponsePart part = new FleetstartResultResponsePart(result);
		resp.addPart(part);
		
		return resp;
	}
}
