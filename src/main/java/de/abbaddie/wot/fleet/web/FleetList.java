package de.abbaddie.wot.fleet.web;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.abbaddie.wot.fleet.data.fleet.EditableFleet;
import de.abbaddie.wot.fleet.data.fleet.Fleet;
import de.abbaddie.wot.fleet.data.fleet.FleetCancelService;
import de.abbaddie.wot.fleet.data.fleet.FleetRepository;
import de.abbaddie.wot.fleet.web.lib.FleetlistCancelResultResponsePart;
import de.abbaddie.wot.fleet.web.lib.FleetlistResponsePart;
import de.abbaddie.wot.web.LoggedInController;
import de.abbaddie.wot.web.lib.JsonResponse;

@Controller
@RequestMapping("/ajax")
public class FleetList extends LoggedInController {
	private static final Logger logger = LoggerFactory.getLogger(FleetList.class);
	
	@Autowired
	protected FleetRepository fleetRepo;
	
	@Autowired
	protected FleetCancelService cancelService;
	
	@RequestMapping("/fleetlist")
	@ResponseBody
	public JsonResponse list() {
		JsonResponse resp = new JsonResponse();
		performDefaultActions(resp);
		
		FleetlistResponsePart part = new FleetlistResponsePart();
		part.addFleets(getFleets());
		resp.addPart(part);
		
		return resp;
	}
	
	@RequestMapping("/fleetlist/cancel")
	@ResponseBody
	public JsonResponse cancel(@RequestParam("fleetId") int fleetId) {
		JsonResponse resp = new JsonResponse();
		performDefaultActions(resp);
		
		try {
			EditableFleet fleet = (EditableFleet) fleetRepo.findOne(fleetId);
			
			if(fleet == null || !fleet.getStartPlanet().getOwner().equals(getUser())) {
				resp.addPart(new FleetlistCancelResultResponsePart("Fehlende Rechte."));
			} else {
				cancelService.cancel(fleet, DateTime.now());
				resp.addPart(new FleetlistCancelResultResponsePart());
				
				FleetlistResponsePart part = new FleetlistResponsePart();
				part.addFleets(getFleets());
				resp.addPart(part);
			}
		} catch(IllegalArgumentException e) {
			resp.addPart(new FleetlistCancelResultResponsePart("Flotte nicht zurückrufbar."));
		} catch(Exception e) {
			logger.warn("fleet cancellation not possible", e);
			resp.addPart(new FleetlistCancelResultResponsePart("Unbekannter Fehler."));
		}
		return resp;
	}
	
	public List<? extends Fleet> getFleets() {
		return fleetRepo.findByOwner(getUser());
	}
}
