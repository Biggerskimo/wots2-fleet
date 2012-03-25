package de.abbaddie.wot.fleet.data.start;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import de.abbaddie.wot.data.spec.Spec;
import de.abbaddie.wot.data.spec.filter.PositiveFilter;
import de.abbaddie.wot.fleet.data.fleet.FleetRepository;
import de.abbaddie.wot.fleet.data.mission.Espionage;
import de.abbaddie.wot.fleet.data.spec.trait.Espionaging;

public class EspionageFleetStarterValidator implements
		ConstraintValidator<EspionageFleetStarterConstraint, EspionageFleetStarterImpl> {
	@Autowired
	protected FleetRepository fleetRepo;
	
	protected ConstraintValidatorContext context;
	protected boolean errorFound = false;
	
	@Override
	public void initialize(EspionageFleetStarterConstraint constraintAnnotation) {
		
	}
	
	protected void addError(String message) {
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		errorFound = true;
	}
	
	@Override
	public boolean isValid(EspionageFleetStarterImpl starter, ConstraintValidatorContext context) {
		this.context = context;
		
		for(Spec spec : starter.getSpecs().filter(new PositiveFilter())) {
			if(!(spec instanceof Espionaging)) {
				addError("Es wurden andere Schiffe denn Spionagesonden versendet.");
			}
		}
		if(!new Espionage().equals(starter.getMission())) {
			addError("Es wurde ein anderer Auftrag denn Spionage angewählt.");
		}
		if(starter.getResources().sum() > 0) {
			addError("Es dürfen keine Rohstoffe mitgenommen werden.");
		}
		
		return !errorFound;
	}
	
}