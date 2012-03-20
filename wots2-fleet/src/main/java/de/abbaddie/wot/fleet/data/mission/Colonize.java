package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("colonize")
@Entity
public class Colonize extends Mission {
	public Colonize() {
		super(9, "Kolonisierung");
	}
}
