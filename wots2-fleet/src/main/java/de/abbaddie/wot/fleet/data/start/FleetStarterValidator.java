package de.abbaddie.wot.fleet.data.start;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import de.abbaddie.wot.data.coordinates.Coordinates;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.event.EventBus;
import de.abbaddie.wot.event.ListProcessor;
import de.abbaddie.wot.fleet.data.fleet.FleetRepository;
import de.abbaddie.wot.fleet.data.spec.trait.FleetSlotProvider;

public class FleetStarterValidator implements ConstraintValidator<FleetStarterConstraint, FleetStarterImpl> {
	@Autowired
	protected FleetRepository fleetRepo;
	
	protected ConstraintValidatorContext context;
	protected boolean errorFound = false;
	
	@Override
	public void initialize(FleetStarterConstraint constraintAnnotation) {
		
	}
	
	protected void addError(String message) {
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		errorFound = true;
	}
	
	@Override
	public boolean isValid(FleetStarterImpl starter, ConstraintValidatorContext context) {
		errorFound = false;
		this.context = context;
		
		if(starter.getSpecs().isEmpty()) {
			addError("Es wurden keine Schiffe ausgewählt.");
		}
		
		if(starter.getStartPlanet() == null) {
			addError("Es wurde kein Startplanet gesetzt.");
		}
		
		if(starter.getStartPlanet().equals(starter.getTargetPlanet())) {
			addError("Der Startplanet kann nicht angeflogen werden.");
		}
		
		if(!starter.getStartPlanet().getSpecs().swallows(starter.getSpecs())) {
			addError("Es wurden zu viele Schiffe ausgewählt.");
		}
		
		if(starter.getStartPlanet().getOwner().isInVacationMode()) {
			addError("Du bist im Urlaubsmodus.");
		}
		
		if(starter.getTargetPlanet() != null && starter.getTargetPlanet().getOwner() != null
				&& starter.getTargetPlanet().getOwner().isInVacationMode()) {
			addError("Das Ziel befindet sich im Urlaubsmodus.");
		}
		
		if(!starter.getStartPlanet().getOwner().isAdmin() && starter.getTargetPlanet() != null
				&& starter.getTargetPlanet().getOwner() != null && starter.getTargetPlanet().getOwner().isAdmin()) {
			addError("Das Ziel befindet sich im Admin-Schutz.");
		}
		
		Coordinates coords = starter.getCoordinates();
		if(coords.getGalaxy() < 1 || coords.getGalaxy() > 4 || coords.getSystem() < 1 || coords.getSystem() > 499
				|| coords.getOrbit() < 1 || coords.getOrbit() > 15) {
			addError("Die Zielkoordinaten außerhalb des anfliegbaren Bereichs.");
		}
		
		if(starter.getMission() == null || !starter.getAllowedMissions().contains(starter.getMission())) {
			addError("Es wurde kein gültiger Auftrag ausgewählt.");
		}
		
		if(!starter.getStartPlanet().getResources().swallows(starter.getUsedResources())) {
			addError("Es sind nicht genügend Rohstoffe auf dem Planeten vorhanden.");
		}
		
		if(starter.getUsedResources().sum() > starter.getCapacity()) {
			addError("Die Ladekapazität ist nicht ausreichend.");
		}
		
		SpecSet<FleetSlotProvider> providers = starter.getStartPlanet().getUnifiedSpecs()
				.filter(FleetSlotProvider.class);
		long slots = 0;
		for(FleetSlotProvider provider : providers) {
			slots += provider.getSlots();
		}
		
		if(fleetRepo.findByOwner(starter.getStartPlanet().getOwner()).size() > slots) {
			addError("Alle Slots sind belegt!");
		}
		
		List<String> additional = EventBus.run(new FleetstartValidationEvent(), starter, new ListProcessor<String>());
		
		for(String error : additional) {
			if(error != null && !error.isEmpty()) {
				addError(error);
			}
		}
		
		return !errorFound;
	}
	
}