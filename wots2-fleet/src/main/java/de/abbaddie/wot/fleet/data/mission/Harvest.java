package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("harvest")
@Entity
public class Harvest extends Mission {
	public Harvest() {
		super(6, "Spionage");
	}
}
