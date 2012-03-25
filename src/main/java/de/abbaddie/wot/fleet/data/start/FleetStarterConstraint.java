package de.abbaddie.wot.fleet.data.start;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FleetStarterValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FleetStarterConstraint {
	String message() default "Sonstiger Fehler";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
