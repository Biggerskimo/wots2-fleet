package de.abbaddie.wot.fleet.data.ovent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.abbaddie.wot.data.ovent.EditableOvent;
import de.abbaddie.wot.data.ovent.OventRepository;
import de.abbaddie.wot.fleet.data.fleet.DefaultEventTypes;
import de.abbaddie.wot.fleet.data.fleet.Fleet;
import de.abbaddie.wot.fleet.data.ovent.FleetOventNature.FleetOventFleet;

@Service
public class FleetOventServiceImpl implements FleetOventService {
	@Autowired
	protected OventRepository oventRepo;
	
	@Transactional
	@Override
	public void createAll(Fleet fleet) {
		if(fleet.getEvents().get(DefaultEventTypes.IMPACT) != null) {
			createImpactOwner(fleet);
		}
		
		if(fleet.getEvents().get(DefaultEventTypes.IMPACT) != null && fleet.getTargetPlanet() != null
				&& fleet.getTargetPlanet().getOwner() != null) {
			createImpactOfiara(fleet);
		}
		
		createReturnOwner(fleet);
	}
	
	protected void createImpactOwner(Fleet fleet) {
		FleetOventFleet ofleet = createFleetOventFleet(fleet, true);
		EditableOvent ovent = oventRepo.create();
		
		ovent.setTime(fleet.getEvents().get(DefaultEventTypes.IMPACT).getTime());
		ovent.setEvent(fleet.getEvents().get(DefaultEventTypes.IMPACT));
		ovent.setPlanet(fleet.getTargetPlanet());
		ovent.setUser(fleet.getStartPlanet().getOwner());
		ovent.setRelationalId(fleet.getId());
		ovent.setNature(new FleetOventNature(ofleet));
		
		oventRepo.update(ovent);
	}
	
	protected void createImpactOfiara(Fleet fleet) {
		FleetOventFleet ofleet = createFleetOventFleet(fleet, false);
		EditableOvent ovent = oventRepo.create();
		
		ovent.setTime(fleet.getEvents().get(DefaultEventTypes.IMPACT).getTime());
		ovent.setEvent(fleet.getEvents().get(DefaultEventTypes.IMPACT));
		ovent.setPlanet(fleet.getTargetPlanet());
		ovent.setUser(fleet.getTargetPlanet().getOwner());
		ovent.setRelationalId(fleet.getId());
		ovent.setNature(new FleetOventNature(ofleet));
		
		oventRepo.update(ovent);
	}
	
	protected void createReturnOwner(Fleet fleet) {
		FleetOventFleet ofleet = createFleetOventFleet(fleet, true);
		EditableOvent ovent = oventRepo.create();
		
		ovent.setTime(fleet.getEvents().get(DefaultEventTypes.RETURN).getTime());
		ovent.setEvent(fleet.getEvents().get(DefaultEventTypes.RETURN));
		ovent.setPlanet(fleet.getStartPlanet());
		ovent.setUser(fleet.getStartPlanet().getOwner());
		ovent.setRelationalId(fleet.getId());
		ovent.setNature(new FleetOventNature(ofleet));
		
		oventRepo.update(ovent);
	}
	
	protected static FleetOventFleet createFleetOventFleet(Fleet fleet, boolean own) {
		FleetOventFleet ofleet = new FleetOventFleet();
		
		ofleet.setCssClass(getCssClass(fleet, own));
		ofleet.setFleetId(fleet.getId());
		ofleet.setMissionId(fleet.getMission().getMissionId());
		ofleet.setOwnerId(fleet.getStartPlanet().getOwner().getId());
		ofleet.setPassage("flight");
		ofleet.setResources(fleet.getResources());
		ofleet.setSpecs(fleet.getSpecs());
		ofleet.setStartCoords(fleet.getStartPlanet().getCoordinates());
		ofleet.setStartPlanetId(fleet.getStartPlanet().getId());
		ofleet.setStartPlanetName(fleet.getStartPlanet().getName());
		
		if(fleet.getTargetPlanet() != null) {
			if(fleet.getTargetPlanet().getOwner() != null) {
				ofleet.setOfiaraId(fleet.getTargetPlanet().getOwner().getId());
			}
			ofleet.setTargetCoords(fleet.getTargetPlanet().getCoordinates());
			ofleet.setTargetPlanetId(fleet.getTargetPlanet().getId());
			ofleet.setTargetPlanetName(fleet.getTargetPlanet().getName());
		}
		
		return ofleet;
	}
	
	// das gehoert egtl in die view ;)
	protected static String getCssClass(Fleet fleet, boolean own) {
		String str = "";
		switch(fleet.getMission().getMissionId()) {
			case 1:
				str = "attack";
				break;
			case 3:
				str = "transport";
				break;
			case 4:
				str = "deploy";
				break;
			case 5:
				str = "destroy";
				break;
			case 6:
				str = "espionage";
				break;
			case 8:
				str = "harvest";
				break;
			case 9:
				str = "colony";
				break;
			case 11:
				str = "federation";
				break;
			case 12:
				str = "hold";
				break;
			case 20:
				str = "missile";
				break;
			default:
				str = "catattack";
		}
		
		return str;
	}
}
